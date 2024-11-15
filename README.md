## 🎯 구현 기능 목록

### 📌 물품 리스트 관리

- [x]  상품명, 가격, 재고, 프로모션 이름 순으로 출력

   ```markdown
   - 콜라 1,000원 10개 탄산2+1
   - 콜라 1,000원 10개
   - 사이다 1,000원 8개 탄산2+1
   - 사이다 1,000원 7개
   - 오렌지주스 1,800원 9개 MD추천상품
   - 오렌지주스 1,800원 재고 없음
   - 탄산수 1,200원 5개 탄산2+1
   - 탄산수 1,200원 재고 없음
   - 물 500원 10개
   - 비타민워터 1,500원 6개
   - 감자칩 1,500원 5개 반짝할인
   - 감자칩 1,500원 5개
   - 초코바 1,200원 5개 MD추천상품
   - 초코바 1,200원 5개
   - 에너지바 2,000원 5개
   - 정식도시락 6,400원 8개
   - 컵라면 1,700원 1개 MD추천상품
   - 컵라면 1,700원 10개
   ```

    - [x]  재고가 0개라면 `재고 없음` 출력
    - [x]  프로모션 재고만 있고 비 프로모션 재고가 없는 경우 비 프로모션 재고를 생성하여 재고 없음 출력

**🚫 입력 예외 처리**

- [x]  물건의 재고가 전부다 0개일 경우 `IllegalArgumentException`

---

### 📌 구매할 상품과 수량

- [x]  구매할 상품과 수량을 입력 받는다.

   ```markdown
   [콜라-10],[사이다-3]
   ```

    - [x]  상품명과 수량은 하이픈`-` 으로 구분한다.
    - [x]  각 상품은 대괄호`[]` 로 묶어 쉼표`,` 로 구분한다.

**🚫 입력 예외 처리**

- [x]  구매할 상품과 수량 형식이 올바르지 않은 경우 `IllegalArgumentException`

   ```markdown
   [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.
   ```

- [x]  구매할 상품의 재고보다 많은 수량을 입력할 경우 `IllegalArgumentException`
- [x]  보유한 상품이 아닌 상품명을 입력하는 경우 `IllegalArgumentException`

---

### 📌 구매할 상품이 프로모션 적용에 해당하는 경우

- [x]  오늘 날짜가 프로모션 기간 내 포함된 경우에만 할인 적용
    - [x]  프로모션 재고를 우선적으로 차감한다.
- [x]  고객이 프로모션 제품을 덜 가져온 경우
    - [x]  재고가 충분한 경우
        - [x]  **고객에게 안내 메시지 출력**

           ```markdown
           현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
           ```

           ```markdown
           Y or N
           ```

            - [x]  Y: 증정 받을 수 있는 상품을 추가
            - [x]  N: 증정 받을 수 있는 상품 추가 안함

  **🚫 입력 예외 처리**

    - [x]  Y / N 을 제외한 다른 입력을 받을 경우 `IllegalArgumentException`

       ```markdown
       [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
       ```


    - [x]  재고가 부족한 경우
        - [x]  **고객에게 안내 메시지 출력**
            
            ```markdown
            현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
            ```
            
            ```markdown
            Y or N
            ```
            
            - [x]  Y: 프로모션이 적용 안되는 제품 정가로 결제
            - [x]  N: 프로모션이 적용 안되는 제품 제외 후 결제
    
    **🚫 입력 예외 처리**
    
    - [x]  Y / N 을 제외한 다른 입력을 받을 경우 `IllegalArgumentException`
        
        ```markdown
        [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
        ```


---

### 📌 멤버십 할인 적용 여부

- [x]  프로모션이 적용되지 않은 제품에 30% 할인 적용
    - [ ]  최대 할인 한도는 8,000원
- [x]  **안내 문구 출력**

   ```markdown
   멤버십 할인을 받으시겠습니까? (Y/N)
   ```

    - [x]  멤버십 할인 적용 여부 입력

       ```markdown
       Y on N
       ```

        - [x]  Y: 멤버십 할인 적용
        - [x]  N: 멤버십 할인 미적용


**🚫 입력 예외 처리**

- [x]  Y / N 을 제외한 다른 입력을 받을 경우 `IllegalArgumentException`

   ```markdown
   [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
   ```


---

### 📌 영수증 출력

- [x]  구매 상품 내역, 증정 상품 내역, 금액 정보를 출력

   ```markdown
   ===========W 편의점=============
   상품명		수량	금액
   콜라		3 	3,000
   에너지바 		5 	10,000
   ===========증	정=============
   콜라		1
   ==============================
   총구매액		8	13,000
   행사할인			-1,000
   멤버십할인			-3,000
   내실돈			 9,000
   ```

    - [x]  결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리

---

### 📌 추가 구매 여부

- [x]  추가 구매 여부 안내 문구 출력

   ```markdown
   감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
   ```

    - [x]  추가 구매 여부 입력

       ```markdown
       Y or N
       ```

        - [x]  Y: 재고가 업데이트된 상품 목록 확인 후 추가 구매 진행
        - [x]  N: 구매 종료


**🚫 입력 예외 처리**

- [x]  Y / N 을 제외한 다른 입력을 받을 경우 `IllegalArgumentException`

   ```markdown
   [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
   ```


---

### 📌 프로그래밍 요구 사항

- [x]  사용자가 잘못된 값을 입력할 경우 `IllegalArgumentException`를 발생시키고, "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는다.
- [x]  영수증 출력 후 추가 구매를 진행할지 또는 종료할지를 선택할 수 있다.
- [x]  `build.gradle` 파일은 변경할 수 없으며, **제공된 라이브러리 이외의 외부 라이브러리는 사용하지 않는다.**
- [x]  프로그램 종료 시 `System.exit()`를 호출하지 않는다.
- [x]  indent(인덴트, 들여쓰기) depth를 3이 넘지 않도록 구현한다. 2까지만 허용한다.
- [x]  3항 연산자를 쓰지 않는다.
- [x]  else 예약어를 쓰지 않는다.
    - [x]  switch/case도 허용하지 않는다.
- [x]  Java Enum을 적용하여 프로그램을 구현한다.
- [x]  구현한 기능에 대한 단위 테스트를 작성한다. 단, UI(System.out, System.in, Scanner) 로직은 제외한다.
- [x]  함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현한다.
- [x]  입출력을 담당하는 클래스를 별도로 구현한다.
    - [x]  아래 `InputView`, `OutputView` 클래스를 참고하여 입출력 클래스를 구현한다.
    - [x]  클래스 이름, 메소드 반환 유형, 시그니처 등은 자유롭게 수정할 수 있다.

       ```java
       public class InputView {
           public String readItem() {
               System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
               String input = Console.readLine();
               // ...
           }
           // ...
       }
       
       ```

       ```java
       public class OutputView {
           public void printProducts() {
               System.out.println("- 콜라 1,000원 10개 탄산2+1");
               // ...
           }
           // ...
       }
       ```

- [x]  `camp.nextstep.edu.missionutils`에서 제공하는 `DateTimes` 및 `Console` API를 사용하여 구현해야 한다,
    - [x]  현재 날짜와 시간을 가져오려면 `camp.nextstep.edu.missionutils.DateTimes`의 `now()`를 활용한다.
    - [x]  사용자가 입력하는 값은 `camp.nextstep.edu.missionutils.Console`의 `readLine()`을 활용한다.

---

### ⚠️ 예외 처리시 고려 사항

- [x]  예외 상황 시 에러 문구를 출력해야 한다. 단, 에러 문구는 "[ERROR]"로 시작해야 한다.

   ```markdown
   [ERROR] 상품명이나 수량을 잘못 입력하셨습니다.
   ```

- [x]  사용자가 잘못된 값을 입력한 경우 에러 메시지 출력 후 그 부분부터 다시 입력 받는다.

   ```markdown
   안녕하세요. W편의점입니다.
   현재 보유하고 있는 상품입니다.
   
   - 콜라 1,000원 10개 탄산2+1
   - 콜라 1,000원 10개
   - 사이다 1,000원 8개 탄산2+1
   - 사이다 1,000원 7개
   - 오렌지주스 1,800원 9개 MD추천상품
   - 오렌지주스 1,800원 재고 없음
   - 탄산수 1,200원 5개 탄산2+1
   - 탄산수 1,200원 재고 없음
   - 물 500원 10개
   - 비타민워터 1,500원 6개
   - 감자칩 1,500원 5개 반짝할인
   - 감자칩 1,500원 5개
   - 초코바 1,200원 5개 MD추천상품
   - 초코바 1,200원 5개
   - 에너지바 2,000원 5개
   - 정식도시락 6,400원 8개
   - 컵라면 1,700원 1개 MD추천상품
   - 컵라면 1,700원 10개
   
   구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
   [싸이다-2],[감자칩-1]
   
   [ERROR] 상품명이나 수량을 잘못 입력하셨습니다.
   
   구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
   [사이다-2],[감자칩-1]
   ```


---

### 👀 전체 출력 예시

```markdown
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-3],[에너지바-5]

멤버십 할인을 받으시겠습니까? (Y/N)
Y 

==============W 편의점================
상품명		수량	금액
콜라		3 	3,000
에너지바 		5 	10,000
=============증	정===============
콜라		1
====================================
총구매액		8	13,000
행사할인			-1,000
멤버십할인			-3,000
내실돈			 9,000

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 7개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-10]

현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
N

==============W 편의점================
상품명		수량	금액
콜라		10 	10,000
=============증	정===============
콜라		2
====================================
총구매액		10	10,000
행사할인			-2,000
멤버십할인			-0
내실돈			 8,000

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 재고 없음 탄산2+1
- 콜라 1,000원 7개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[오렌지주스-1]

현재 오렌지주스은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
Y

==============W 편의점================
상품명		수량	금액
오렌지주스		2 	3,600
=============증	정===============
오렌지주스		1
====================================
총구매액		2	3,600
행사할인			-1,800
멤버십할인			-0
내실돈			 1,800

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
N
```

## 프로모션 생각해보기

### **1. 사용자가 구매할 상품명과 수량 입력**

- 시스템은 사용자가 입력한 상품명을 기준으로:
    - 해당 상품이 **프로모션 적용 대상인지** 확인.
    - 해당 상품의 **프로모션 재고**와 **비 프로모션 재고**를 조회.
    - 프로모션 제품은 덤으로 주는게 아닌 구매 후 할인으로 빠짐
        - 즉, 1+1의 경우 2개를 구매하고 추후 1개를 할인해주는 것
        - 2+1의 경우 3개를 구매하고 추후 1개를 할인해주는 것

    ---


### **2. 총 재고 확인 및 초과 여부 판단**

- 입력한 수량이 **총 재고(프로모션 재고 + 비 프로모션 재고)**를 초과하는지 확인.
    - **초과한 경우**:
        - 메시지 출력:

            ```scss
            입력한 수량(X개)이 총 재고(Y개)를 초과합니다. 구매 가능한 최대 수량은 Y개입니다.
            ```

        - 수량 재입력 요청.
- 입력한 수량이 **총 재고 이내인 경우**:
    - 다음 단계로 진행.

### **3. 프로모션 대상 여부 확인**

- 상품이 **프로모션 적용 대상인 경우**:
    - 프로모션 유형(1+1, 2+1 등)을 확인.
    - **4. 입력한 상품의 수량과 프로모션 재고 비교**로 이동.
- 상품이 **프로모션 적용 대상이 아닌 경우**:
    - "해당 상품은 프로모션 적용 대상이 아닙니다. 구매를 진행합니다."라는 메시지와 함께 **비 프로모션 재고**에서 구매 처리.

---

### **4. 입력한 상품의 수량과 프로모션 재고 비교**

- **입력한 수량 ≤ 프로모션 재고**:
    - **5. 프로모션 적용 여부 판단**으로 이동.
- **입력한 수량 > 프로모션 재고**:
    - **프로모션 재고 내에서는 프로모션을 적용**.
    - 초과된 수량은 **비 프로모션 재고로 구매 처리**.
        - 초과된 수량은 **비 프로모션 재고**로 처리해야 하므로 기록.
        - 프로모션 재고 초과 수량은 최종 출력 시 사용자에게 안내.
    - **5. 프로모션 적용 여부 판단**으로 이동.

---

### **5. 프로모션 적용 여부 판단 (1+1 또는 2+1)**

- **1+1 프로모션의 경우**:
    - **프로모션 재고가 홀수인 상황 (ex: 7개, 9개)**:
        - 사용자가 입력한 수량이 **프로모션 재고와 동일**할 경우:
            - 현재 (상품명) (promotions.md의 buy)개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
            - **Y/N 입력 요구**:
                - Y: 구매를 진행하며 비 프로모션으로 처리.
                - N: 구매 취소.
        - 사용자가 입력한 수량이 **짝수**일 경우:
            - 프로모션을 정상적으로 적용하고 구매 처리.
- **2+1 프로모션의 경우**:
    - **프로모션 재고가 특수한 상황 (ex: 2, 5, 8, 11과 같이 2부터 3씩 늘어나는 경우)**:
        - 사용자가 입력한 수량이 **프로모션 재고와 동일**할 경우:
            - "현재 (상품명) (promotions.md의 buy)개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"라고 안내.
            - **Y/N 입력 요구**:
                - Y: 구매를 진행하며 비 프로모션으로 처리.
                - N: 구매 취소.
        - 사용자가 입력한 수량이 **프로모션 재고보다 적은 경우**:
            - 프로모션을 정상적으로 적용하고 구매 처리.

---

### **6. 프로모션 로직 적용**

- **1+1 프로모션**:
    - 입력한 수량이 짝수인 경우:
        - 안내 없이 프로모션 처리.
    - 입력한 수량이 홀수인 경우(입력한 수량이 프로모션 재고보다 적은 경우):
        - "현재 (상품명)은(는) (promotions.md get)개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"라고 안내.
        - **Y/N 입력 요구**:
            - Y: 추가 1개를 구매로 처리하며 프로모션 재고에서 차감.
            - N: 입력한 수량만 구매 처리.
- **2+1 프로모션**:
    - 입력한 수량이 (1, 4, 7, 10 등) 1부터 3씩 증가하는 경우:
        - 1개는 정가로 처리
        - 1개를 제외한 나머지는 프로모션 처리
    - 입력한 수량이 (2, 5, 8, 11 등) 2부터 3씩 증가하는 경우:
        - "현재 (상품명)은(는) (promotions.md get)개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"라고 안내.
        - **Y/N 입력 요구**:
            - Y: 추가 1개를 구매로 처리하며 프로모션 재고에서 차감.
            - N: 입력한 수량만 구매 처리.
    - 입력한 수량이 (3, 6, 9, 12 등) 3부터 3씩 증가하는 경우:
        - 안내 없이 구매 처리.

---

### **7. 구매 최종 처리**

- **프로모션 적용**:
    - 구매한 수량만큼 프로모션 재고에서 차감.
    - 프로모션으로 가져간 수량도 기록.
- **비 프로모션 적용**:
    - 초과된 수량만큼 비 프로모션 재고에서 차감.
- **사용자에게 최종 결과 안내**:
    - "프로모션 적용: X개, 비 프로모션 적용: Y개"와 같은 형식으로 결과를 출력.

---

### **8. 예외 처리**

- **사용자 입력 예외 처리**:
    - **Y/N 입력 요구 상황**에서 잘못된 입력이 들어오면:
        - "잘못된 입력입니다. Y 또는 N을 입력하세요."라는 메시지 출력 후 다시 입력받음.
- **재고 부족 상황**:
    - 입력한 수량이 총 재고(프로모션 + 비 프로모션)보다 많은 경우:
        - "재고가 부족합니다. 구매할 수 있는 최대 수량은 Z개입니다."라는 안내 후 재 입력하도록 함

```markdown
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-20],[오렌지주스-9],[사이다-5],[물-3],[탄산수-5]

현재 콜라 11개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
y

현재 오렌지주스 1개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
y

현재 사이다은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
y

현재 탄산수 2개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
y

==============W 편의점================
상품명		수량	금액
콜라		20 	20,000
오렌지주스		9	16,200
사이다		6	6,000
물		3	1,500
탄산수		5	6,000
=============증	정===============
콜라		3
오렌지주스		4
사이다		2
탄산수		1
====================================
총구매액		43	49,700
행사할인			-13,400
내실돈			 36,300
```