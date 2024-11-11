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
            processPurchase(products, promotions);
            shopping = askForMoreShopping();
        }
    }

    public static void initializeProducts(List<Product> products) {
        String productsFilePath = "products.md";
        String expectedHeader = "name,price,quantity,promotion";

        List<String[]> productData = readMarkdownFile(productsFilePath, expectedHeader);
        Map<String, List<Product>> groupedProducts = parseAndGroupProducts(productData);

        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            String name = entry.getKey();
            List<Product> productGroup = entry.getValue();

            products.addAll(productGroup);

            if (!hasNonPromotionalProduct(productGroup)) {
                Product lastProduct = productGroup.get(0);
                products.add(createNonPromotionalProduct(name, lastProduct));
            }
        }
    }

    private static Map<String, List<Product>> parseAndGroupProducts(List<String[]> productData) {
        Map<String, List<Product>> groupedProducts = new LinkedHashMap<>();
        for (String[] parts : productData) {
            Product product = parseProduct(parts);
            groupedProducts.computeIfAbsent(product.getName(), k -> new ArrayList<>()).add(product);
        }
        return groupedProducts;
    }

    private static Product parseProduct(String[] parts) {
        String name = parts[0];
        int price = Integer.parseInt(parts[1]);
        int stock = Integer.parseInt(parts[2]);
        String promotion = (parts.length > 3 && !parts[3].equalsIgnoreCase("null")) ? parts[3] : null;
        return new Product(name, price, stock, promotion);
    }

    private static boolean hasNonPromotionalProduct(List<Product> productGroup) {
        return productGroup.stream().anyMatch(p -> p.getPromotion() == null);
    }

    private static Product createNonPromotionalProduct(String name, Product lastProduct) {
        return new Product(name, lastProduct.getPrice(), lastProduct.getStock(), null);
    }

    public static Map<String, Promotion> initializePromotions() {
        String promotionsFilePath = "promotions.md";
        String expectedHeader = "name,buy,get,start_date,end_date";

        List<String[]> promotionData = readMarkdownFile(promotionsFilePath, expectedHeader);
        return parsePromotionData(promotionData);
    }

    private static Map<String, Promotion> parsePromotionData(List<String[]> promotionData) {
        Map<String, Promotion> promotions = new HashMap<>();
        for (String[] parts : promotionData) {
            Promotion promotion = parsePromotion(parts);
            promotions.put(promotion.getName(), promotion);
        }
        return promotions;
    }

    private static Promotion parsePromotion(String[] parts) {
        String name = parts[0];
        int buy = Integer.parseInt(parts[1]);
        int get = Integer.parseInt(parts[2]);
        return new Promotion(name, buy, get);
    }

    public static List<String[]> readMarkdownFile(String filename, String expectedHeader) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Application.class.getClassLoader().getResourceAsStream(filename)))) {

            if (reader == null) {
                throw new IllegalArgumentException("[ERROR] 파일을 찾을 수 없습니다.: " + filename);
            }
            validateHeader(reader.readLine(), expectedHeader, filename);
            return readLines(reader);

        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] 파일을 읽을 수 없습니다.: " + filename);
        }
    }

    public static void validateHeader(String headerLine, String expectedHeader, String filename) {
        if (headerLine == null || !headerLine.trim().equals(expectedHeader)) {
            throw new IllegalArgumentException("[ERROR] 파일 헤더가 예상과 다릅니다. 파일명: " + filename);
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
        List<PurchaseRequest> purchaseRequests = new ArrayList<>();
        boolean purchaseComplete = false;
        while (!purchaseComplete) {
            try {
                executePurchase(products, promotions, purchaseRequests);
                purchaseComplete = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void executePurchase(List<Product> products, Map<String, Promotion> promotions, List<PurchaseRequest> purchaseRequests) {
        String[] itemList = getPurchaseItems();
        List<PurchaseRequest> tempRequests = parsePurchaseItems(itemList, products, promotions);

        processPurchaseRequests(products, purchaseRequests, tempRequests);

        int membershipDiscount = applyMembershipDiscount(purchaseRequests);

        printReceipt(purchaseRequests, membershipDiscount);
    }

    private static String[] getPurchaseItems() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String items = Console.readLine();
        return items.split(",");
    }

    private static void processPurchaseRequests(List<Product> products, List<PurchaseRequest> purchaseRequests, List<PurchaseRequest> tempRequests) {
        for (PurchaseRequest request : tempRequests) {
            handlePurchaseRequest(products, request);
            purchaseRequests.add(request);
        }
    }

    private static List<PurchaseRequest> parsePurchaseItems(String[] itemList, List<Product> products, Map<String, Promotion> promotions) {
        List<PurchaseRequest> tempRequests = new ArrayList<>();
        for (String item : itemList) {
            PurchaseRequest request = parsePurchaseItem(item);
            processPurchaseItem(request, products, promotions, tempRequests);
        }
        return tempRequests;
    }

    private static void processPurchaseItem(PurchaseRequest request, List<Product> products, Map<String, Promotion> promotions, List<PurchaseRequest> tempRequests) {
        List<Product> matchedProducts = findProductsByName(products, request.getProductName());
        if (matchedProducts.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 해당 상품을 찾을 수 없습니다: " + request.getProductName());
        }

        int requestedQuantity = request.getQuantity();
        int totalStock = getTotalStock(matchedProducts);

        if (requestedQuantity > totalStock) {
            throw new IllegalArgumentException("[ERROR] 입력한 수량(" + requestedQuantity + "개)이 총 재고(" + totalStock + "개)를 초과합니다. 구매 가능한 최대 수량은 " + totalStock + "개입니다.");
        }

        Product product = matchedProducts.get(0);
        Promotion promotion = promotions.get(product.getPromotion());

        if (promotion == null) {
            setNonPromotionalRequest(request, product, requestedQuantity);
            tempRequests.add(request);
        } else {
            int promoStock = product.getPromotionStock();
            int nonPromoStock = totalStock - promoStock;
            processPromotionAndAddRequests(tempRequests, request, promoStock, nonPromoStock, product, promotion);
        }
    }

    private static int getTotalStock(List<Product> matchedProducts) {
        return matchedProducts.stream().mapToInt(Product::getStock).sum();
    }

    private static void setNonPromotionalRequest(PurchaseRequest request, Product product, int requestedQuantity) {
        request.setPrice(product.getPrice());
        request.setPromotionApplicable(false);
        request.setFinalQuantity(requestedQuantity);
    }

    private static void processPromotionAndAddRequests(
            List<PurchaseRequest> tempRequests,
            PurchaseRequest request,
            int promoStock,
            int nonPromoStock,
            Product product,
            Promotion promotion
    ) {
        int requestedQuantity = request.getQuantity();
        int promotionTotal = promotion.getBuy() + promotion.getGet();
        int maxPromotionCycles = promoStock / promotionTotal;
        int promoApplicableQuantity = Math.min(requestedQuantity, maxPromotionCycles * promotionTotal);
        int nonPromoApplicableQuantity = requestedQuantity - promoApplicableQuantity;

        if (promoApplicableQuantity < requestedQuantity) {
            int nonPromotionalQuantity = requestedQuantity - promoApplicableQuantity;
            handleNonPromoQuantity(nonPromotionalQuantity, product.getName());
        }

        int promoCycleRemainder = promoApplicableQuantity % promotionTotal;
        if (promoCycleRemainder >= promotion.getBuy() && promoCycleRemainder < promotionTotal) {
            promoApplicableQuantity = adjustPromoQuantityForAdditionalPurchase(
                    promoApplicableQuantity,
                    promoCycleRemainder,
                    promotionTotal,
                    promoStock,
                    requestedQuantity,
                    product.getName(),
                    promotion
            );
        }

        request.setPrice(product.getPrice());
        request.setPromotionApplicable(true);
        request.setPromotion(promotion);

        int fullPromotionCycles = promoApplicableQuantity / promotionTotal;
        int giftedQuantity = fullPromotionCycles * promotion.getGet();

        request.setGiftedQuantity(giftedQuantity);
        request.setFinalQuantity(promoApplicableQuantity + nonPromoApplicableQuantity);

        tempRequests.add(request);
    }

    private static int adjustPromoQuantityForAdditionalPurchase(
            int promoApplicableQuantity,
            int promoCycleRemainder,
            int promotionTotal,
            int promoStock,
            int requestedQuantity,
            String productName,
            Promotion promotion
    ) {
        int additionalNeeded = promotionTotal - promoCycleRemainder;
        System.out.println("현재 " + productName + "은(는) " + promotion.getGet() + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        boolean userWantsAdditional = getUserConfirmation();
        if (!userWantsAdditional) {
            return promoApplicableQuantity;
        }
        if (promoApplicableQuantity + additionalNeeded <= promoStock) {
            promoApplicableQuantity += additionalNeeded;
            return promoApplicableQuantity;
        }
        int nonPromoQuantity = requestedQuantity - promoApplicableQuantity;
        handleNonPromoQuantity(nonPromoQuantity, productName);
        return promoApplicableQuantity;
    }

    private static void handleNonPromoQuantity(int nonPromoQuantity, String productName) {
        System.out.println("현재 " + productName + " " + nonPromoQuantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
        boolean proceed = getUserConfirmation();
        if (!proceed) {
            throw new IllegalArgumentException("[ERROR] 구매가 취소되었습니다.");
        }
    }

    private static boolean getUserConfirmation() {
        while (true) {
            String input = Console.readLine().trim().toUpperCase();
            if ("Y".equals(input)) {
                return true;
            }
            if ("N".equals(input)) {
                return false;
            }
            System.out.println("[ERROR] 잘못된 입력입니다. Y 또는 N을 입력하세요.");
        }
    }

    public static void handlePurchaseRequest(List<Product> products, PurchaseRequest request) {
        String productName = request.getProductName();

        int totalQuantity = request.getFinalQuantity() + request.getGiftedQuantity();

        for (Product product : products) {
            if (!product.getName().equals(productName) || product.getStock() <= 0) {
                continue;
            }
            int reducible = Math.min(totalQuantity, product.getStock());
            product.reduceStock(reducible);
            totalQuantity -= reducible;
            if (totalQuantity == 0) {
                break;
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

        matchedProducts.sort((p1, p2) -> {
            if (p1.getPromotion() == null && p2.getPromotion() != null) {
                return 1;
            }
            if (p1.getPromotion() != null && p2.getPromotion() == null) {
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
            if (request.getFinalQuantity() <= 0) {
                continue;
            }
            int cost = request.getFinalQuantity() * request.getPrice();
            totalItems += request.getFinalQuantity();
            totalAmount += cost;
            System.out.println(request.getProductName() + "\t\t" + request.getFinalQuantity() + "\t" + numberFormat.format(cost));
        }

        System.out.println("=============증\t정===============");
        for (PurchaseRequest request : purchaseRequests) {
            if (request.getGiftedQuantity() > 0) {
                System.out.println(request.getProductName() + "\t\t" + request.getGiftedQuantity());
            }
        }

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
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        while (true) {
            String input = Console.readLine().trim().toUpperCase();
            if ("Y".equals(input)) {
                return true;
            }
            if ("N".equals(input)) {
                return false;
            }
            System.out.println("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
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

        return new PurchaseRequest(productName, quantity, 0);
    }

    public static int applyMembershipDiscount(List<PurchaseRequest> purchaseRequests) {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        String input = getValidYesNoInput();

        if ("N".equals(input)) {
            return 0;
        }

        int totalNonPromoPrice = calculateTotalNonPromoPrice(purchaseRequests);
        int membershipDiscount = (int) (totalNonPromoPrice * 0.3);
        if (membershipDiscount > 8000) {
            membershipDiscount = 8000;
        }
        return membershipDiscount;
    }

    private static String getValidYesNoInput() {
        String input = Console.readLine().trim().toUpperCase();
        while (!"Y".equals(input) && !"N".equals(input)) {
            System.out.println("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            input = Console.readLine().trim().toUpperCase();
        }
        return input;
    }

    private static int calculateTotalNonPromoPrice(List<PurchaseRequest> purchaseRequests) {
        int totalNonPromoPrice = 0;
        for (PurchaseRequest request : purchaseRequests) {
            if (request.isPromotionApplicable()) {
                continue;
            }
            totalNonPromoPrice += request.getFinalQuantity() * request.getPrice();
        }
        return totalNonPromoPrice;
    }
}

class Product {
    private final String name;
    private final int price;
    private int stock;
    private final String promotion;
    private int promotionStock;

    public Product(String name, int price, int stock, String promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
        this.promotionStock = 0;
        if (promotion != null) {
            this.promotionStock = stock;
        }
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
        if (stock < quantity) {
            throw new IllegalArgumentException(name + "의 재고가 부족합니다.");
        }
        stock -= quantity;
        if (promotion != null) {
            int promoReducible = Math.min(quantity, promotionStock);
            promotionStock -= promoReducible;
        }
    }

    public void display() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        String promoString = "";
        if (promotion != null) {
            promoString = " " + promotion;
        }

        if (stock == 0) {
            System.out.println("- " + name + " " + numberFormat.format(price) + "원 재고 없음" + promoString);
            return;
        }
        System.out.println("- " + name + " " + numberFormat.format(price) + "원 " + stock + "개" + promoString);
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
    private int quantity;
    private int price;
    private int giftedQuantity;
    private int finalQuantity;

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

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }
}
