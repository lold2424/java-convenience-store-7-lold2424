package store;

import camp.nextstep.edu.missionutils.Console;

import java.text.NumberFormat;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Application {
    public static void main(String[] args) {
        List<Product> products = new ArrayList<>();
        initializeProducts(products);
        Map<String, Promotion> promotions = initializePromotions();

        boolean shopping = true;
        while (shopping) {
            displayProducts(products);
            try {
                processPurchase(products, promotions);
                shopping = askForMoreShopping();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void initializeProducts(List<Product> products) {
        String productsFilePath = "products.md";
        String expectedHeader = "name,price,quantity,promotion";

        List<String[]> productData = readMarkdownFile(productsFilePath, expectedHeader);
        Map<String, List<Product>> groupedProducts = new LinkedHashMap<>();

        // Load all products into a grouped map
        for (String[] parts : productData) {
            String name = parts[0];
            int price = Integer.parseInt(parts[1]);
            int stock = Integer.parseInt(parts[2]);
            String promotion = parts.length > 3 && !parts[3].equalsIgnoreCase("null") ? parts[3] : null;

            Product product = new Product(name, price, stock, promotion);
            groupedProducts.computeIfAbsent(name, k -> new ArrayList<>()).add(product);
        }

        // Create the final sorted list
        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            String name = entry.getKey();
            List<Product> productGroup = entry.getValue();

            // Add all existing products
            products.addAll(productGroup);

            // Check if non-promotional product exists
            boolean hasNonPromotional = productGroup.stream().anyMatch(p -> p.getPromotion() == null);
            if (!hasNonPromotional) {
                // Add a non-promotional product with stock
                Product lastProduct = productGroup.get(0);
                Product nonPromoProduct = new Product(name, lastProduct.getPrice(), lastProduct.getStock(), null);
                products.add(nonPromoProduct);
            }
        }
    }

    public static Map<String, Promotion> initializePromotions() {
        String promotionsFilePath = "promotions.md";
        String expectedHeader = "name,buy,get,start_date,end_date";

        List<String[]> promotionData = readMarkdownFile(promotionsFilePath, expectedHeader);
        Map<String, Promotion> promotions = new HashMap<>();
        for (String[] parts : promotionData) {
            String name = parts[0];
            int buy = Integer.parseInt(parts[1]);
            int get = Integer.parseInt(parts[2]);
            promotions.put(name, new Promotion(name, buy, get));
        }
        return promotions;
    }

    public static List<String[]> readMarkdownFile(String filename, String expectedHeader) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Application.class.getClassLoader().getResourceAsStream(filename)))) {

            if (reader == null) throw new IllegalStateException("[ERROR] 파일을 찾을 수 없습니다.: " + filename);
            validateHeader(reader.readLine(), expectedHeader, filename);
            return readLines(reader);

        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 파일을 읽을 수 없습니다.: " + filename, e);
        }
    }

    public static void validateHeader(String headerLine, String expectedHeader, String filename) {
        if (headerLine == null || !headerLine.trim().equals(expectedHeader)) {
            throw new IllegalStateException("[ERROR] 파일 헤더가 예상과 다릅니다. 파일명: " + filename);
        }
    }

    public static List<String[]> readLines(BufferedReader reader) throws IOException {
        List<String[]> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line.split(","));
        }
        return lines;
    }

    public static void displayProducts(List<Product> products) {
        System.out.println("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n");
        for (Product product : products) {
            product.display();
        }
    }

    public static void processPurchase(List<Product> products, Map<String, Promotion> promotions) {
        List<PurchaseRequest> purchaseRequests = new ArrayList<>(); // 각 거래마다 새로운 리스트 생성
        while (true) {
            System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
            String items = Console.readLine();
            String[] itemList = items.split(",");

            boolean allValid = true;
            List<PurchaseRequest> tempRequests = new ArrayList<>();

            for (String item : itemList) {
                PurchaseRequest request;
                try {
                    request = parsePurchaseItem(item);
                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] 잘못된 입력 형식입니다. 다시 입력해 주세요.");
                    allValid = false;
                    break;
                }

                List<Product> matchedProducts = findProductsByName(products, request.getProductName());
                if (matchedProducts.isEmpty()) {
                    System.out.println("[ERROR] 해당 상품을 찾을 수 없습니다: " + request.getProductName());
                    allValid = false;
                    break;
                }

                int requestedQuantity = request.getQuantity();

                // Get total stock
                int totalStock = matchedProducts.stream().mapToInt(Product::getStock).sum();
                if (requestedQuantity > totalStock) {
                    System.out.println("[ERROR] 입력한 수량(" + requestedQuantity + "개)이 총 재고(" + totalStock + "개)를 초과합니다. 구매 가능한 최대 수량은 " + totalStock + "개입니다.");
                    allValid = false;
                    break;
                }

                // Check promotion applicability
                Product product = matchedProducts.get(0);
                Promotion promotion = promotions.get(product.getPromotion());

                if (promotion != null) {
                    // Product has promotion
                    int promoStock = product.getPromotionStock();
                    int nonPromoStock = totalStock - promoStock;

                    boolean proceed = processPromotionAndAddRequests(tempRequests, request, promoStock, nonPromoStock, product, promotion);
                    if (!proceed) {
                        allValid = false;
                        break;
                    }
                } else {
                    // Product does not have promotion
                    if (requestedQuantity > totalStock) {
                        System.out.println("[ERROR] 재고가 부족합니다. 구매 가능한 최대 수량은 " + totalStock + "개입니다.");
                        allValid = false;
                        break;
                    }

                    // Process non-promotion purchase
                    request.setPrice(product.getPrice());
                    request.setPromotionApplicable(false);
                    request.setFinalQuantity(requestedQuantity);
                    tempRequests.add(request);
                }
            }

            if (allValid) {
                // Confirm purchase
                for (PurchaseRequest request : tempRequests) {
                    handlePurchaseRequest(products, request);
                    purchaseRequests.add(request);
                }

                // 멤버십 할인 적용 여부 묻기
                int membershipDiscount = applyMembershipDiscount(purchaseRequests);

                // 영수증 출력
                printReceipt(purchaseRequests, membershipDiscount);
                break;
            } else {
                System.out.println("[ERROR] 잘못된 입력이 있습니다. 다시 입력해 주세요.\n");
            }
        }
    }

    private static boolean processPromotionAndAddRequests(
            List<PurchaseRequest> tempRequests,
            PurchaseRequest request,
            int promoStock,
            int nonPromoStock,
            Product product,
            Promotion promotion
    ) {
        int requestedQuantity = request.getQuantity();
        int totalStock = promoStock + nonPromoStock;

        // Calculate how many promotions can be applied
        int promotionTotal = promotion.getBuy() + promotion.getGet();
        int maxPromotionCycles = promoStock / promotionTotal;

        int promoApplicableQuantity = Math.min(requestedQuantity, maxPromotionCycles * promotionTotal);
        int nonPromoApplicableQuantity = requestedQuantity - promoApplicableQuantity;

        // If there is remaining promo stock but not enough to form a full promotion cycle
        int remainingPromoStock = promoStock - promoApplicableQuantity;
        int promoRemainder = remainingPromoStock;

        if (promoApplicableQuantity < requestedQuantity) {
            // There are quantities that cannot be applied with promotion
            int nonPromotionalQuantity = requestedQuantity - promoApplicableQuantity;

            System.out.println("현재 " + product.getName() + " " + nonPromotionalQuantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
            if (!getUserConfirmation()) {
                return false;
            }
        }

        // Check if user wants to add more items to complete a promotion cycle
        int promoCycleRemainder = promoApplicableQuantity % promotionTotal;
        if (promoCycleRemainder >= promotion.getBuy() && promoCycleRemainder < promotionTotal) {
            int additionalNeeded = promotionTotal - promoCycleRemainder;
            System.out.println("현재 " + product.getName() + "은(는) " + promotion.getGet() + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
            if (getUserConfirmation()) {
                if (promoApplicableQuantity + additionalNeeded <= promoStock) {
                    promoApplicableQuantity += additionalNeeded;
                } else {
                    int nonPromoQuantity = requestedQuantity - promoApplicableQuantity;
                    System.out.println("현재 " + product.getName() + " " + nonPromoQuantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
                    if (!getUserConfirmation()) {
                        return false;
                    }
                }
            }
        }

        request.setPrice(product.getPrice());
        request.setPromotionApplicable(true);
        request.setPromotion(promotion);

        int fullPromotionCycles = promoApplicableQuantity / promotionTotal;
        int giftedQuantity = fullPromotionCycles * promotion.getGet();

        request.setGiftedQuantity(giftedQuantity);
        request.setFinalQuantity(promoApplicableQuantity + nonPromoApplicableQuantity);

        tempRequests.add(request);
        return true;
    }

    private static boolean getUserConfirmation() {
        while (true) {
            String input = Console.readLine().trim().toUpperCase();
            if (input.equals("Y")) {
                return true;
            } else if (input.equals("N")) {
                return false;
            } else {
                System.out.println("[ERROR] 잘못된 입력입니다. Y 또는 N을 입력하세요.");
            }
        }
    }

    public static void handlePurchaseRequest(List<Product> products, PurchaseRequest request) {
        String productName = request.getProductName();

        int totalQuantity = request.getFinalQuantity() + request.getGiftedQuantity();

        for (Product product : products) {
            if (product.getName().equals(productName) && product.getStock() > 0) {
                int reducible = Math.min(totalQuantity, product.getStock());
                product.reduceStock(reducible);
                totalQuantity -= reducible;
                if (totalQuantity == 0) break;
            }
        }
    }

    public static List<Product> findProductsByName(List<Product> products, String productName) {
        List<Product> matchedProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                matchedProducts.add(product);
            }
        }

        // 프로모션이 있는 제품을 우선적으로 처리하기 위해 정렬합니다.
        matchedProducts.sort((p1, p2) -> {
            if (p1.getPromotion() == null && p2.getPromotion() != null) {
                return 1;
            } else if (p1.getPromotion() != null && p2.getPromotion() == null) {
                return -1;
            }
            return 0;
        });

        return matchedProducts;
    }

    public static void printReceipt(List<PurchaseRequest> purchaseRequests, int membershipDiscount) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        System.out.println("\n==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");

        int totalItems = 0;
        int totalAmount = 0;
        int totalDiscount = 0;

        for (PurchaseRequest request : purchaseRequests) {
            if (request.getFinalQuantity() <= 0) continue; // 구매가 취소된 경우 건너뜁니다.
            int cost = request.getFinalQuantity() * request.getPrice();
            totalItems += request.getFinalQuantity(); // 증정된 상품은 포함하지 않습니다.
            totalAmount += cost;
            System.out.println(request.getProductName() + "\t\t" + request.getFinalQuantity() + "\t" + numberFormat.format(cost));
        }

        // 증정된 상품 출력
        System.out.println("=============증\t정===============");
        int totalGiftedItems = 0;
        for (PurchaseRequest request : purchaseRequests) {
            if (request.getGiftedQuantity() > 0) {
                totalGiftedItems += request.getGiftedQuantity();
                System.out.println(request.getProductName() + "\t\t" + request.getGiftedQuantity());
            }
        }

        // 행사할인 계산 (증정된 상품들의 가격 합계)
        for (PurchaseRequest request : purchaseRequests) {
            if (request.getGiftedQuantity() > 0) {
                int discount = request.getGiftedQuantity() * request.getPrice();
                totalDiscount += discount;
            }
        }

        int finalAmount = totalAmount - totalDiscount - membershipDiscount;

        System.out.println("====================================");
        System.out.println("총구매액\t\t" + totalItems + "\t" + numberFormat.format(totalAmount));
        System.out.println("행사할인\t\t\t-" + numberFormat.format(totalDiscount));
        if (membershipDiscount > 0) {
            System.out.println("멤버십할인\t\t\t-" + numberFormat.format(membershipDiscount));
        }
        System.out.println("내실돈\t\t\t " + numberFormat.format(finalAmount));
    }

    public static boolean askForMoreShopping() {
        while (true) {
            System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
            String input = Console.readLine().trim().toUpperCase();
            if (input.equals("Y")) {
                return true;
            } else if (input.equals("N")) {
                return false;
            } else {
                System.out.println("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }
        }
    }

    public static PurchaseRequest parsePurchaseItem(String item) {
        if (!item.matches("\\[.+-\\d+\\]")) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다: " + item);
        }

        String[] splitItem = item.replaceAll("[\\[\\]]", "").split("-");
        if (splitItem.length != 2) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다: " + item);
        }

        String productName = splitItem[0].trim();
        int quantity;
        try {
            quantity = Integer.parseInt(splitItem[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 수량은 숫자여야 합니다: " + splitItem[1].trim());
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("[ERROR] 수량은 1 이상이어야 합니다: " + quantity);
        }

        return new PurchaseRequest(productName, quantity, 0); // 가격은 추후에 설정
    }

    public static int applyMembershipDiscount(List<PurchaseRequest> purchaseRequests) {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        int membershipDiscount = 0;
        while (true) {
            String input = Console.readLine().trim().toUpperCase();
            if (input.equals("Y")) {
                // 멤버십 할인 적용
                int totalNonPromoPrice = 0;
                for (PurchaseRequest request : purchaseRequests) {
                    if (!request.isPromotionApplicable()) {
                        totalNonPromoPrice += request.getFinalQuantity() * request.getPrice();
                    }
                }
                membershipDiscount = (int) (totalNonPromoPrice * 0.3);
                if (membershipDiscount > 8000) {
                    membershipDiscount = 8000;
                }
                break;
            } else if (input.equals("N")) {
                // 멤버십 할인 미적용
                membershipDiscount = 0;
                break;
            } else {
                System.out.println("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }
        }
        return membershipDiscount;
    }
}

class Product {
    private final String name;
    private final int price;
    private int stock;
    private final String promotion;
    private int promotionStock; // 프로모션 재고

    public Product(String name, int price, int stock, String promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
        this.promotionStock = promotion != null ? stock : 0; // 프로모션이 있는 경우 전체 재고를 프로모션 재고로 설정
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getPromotion() {
        return promotion;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

    public void reduceStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            if (promotion != null) {
                int promoReducible = Math.min(quantity, promotionStock);
                promotionStock -= promoReducible;
            }
        } else {
            throw new IllegalArgumentException(name + "의 재고가 부족합니다.");
        }
    }

    public void display() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        if (stock == 0) {
            System.out.println("- " + name + " " + numberFormat.format(price) + "원 재고 없음" + (promotion != null ? " " + promotion : ""));
        } else {
            System.out.println("- " + name + " " + numberFormat.format(price) + "원 " + stock + "개" + (promotion != null ? " " + promotion : ""));
        }
    }
}

class Promotion {
    private final String name;
    private final int buy;
    private final int get;

    public Promotion(String name, int buy, int get) {
        this.name = name;
        this.buy = buy;
        this.get = get;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }
}

class PurchaseRequest {
    private final String productName;
    private int quantity; // 구매할 수량
    private int price;
    private int giftedQuantity; // 증정된 개수를 저장
    private int finalQuantity; // 최종 구매 수량 (프로모션 적용 후)

    private boolean isPromotionApplicable;
    private Promotion promotion;

    public PurchaseRequest(String productName, int quantity, int price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.giftedQuantity = 0;
        this.finalQuantity = quantity;
        this.isPromotionApplicable = false;
        this.promotion = null;
    }

    public PurchaseRequest(String productName, int quantity, int price, boolean isPromotionApplicable, Promotion promotion) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.giftedQuantity = 0;
        this.finalQuantity = quantity;
        this.isPromotionApplicable = isPromotionApplicable;
        this.promotion = promotion;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public int getGiftedQuantity() {
        return giftedQuantity;
    }

    public void setGiftedQuantity(int giftedQuantity) {
        this.giftedQuantity = giftedQuantity;
    }

    public int getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(int finalQuantity) {
        this.finalQuantity = finalQuantity;
    }

    public boolean isPromotionApplicable() {
        return isPromotionApplicable;
    }

    public void setPromotionApplicable(boolean promotionApplicable) {
        isPromotionApplicable = promotionApplicable;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }
}
