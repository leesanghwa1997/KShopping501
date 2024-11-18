# 요구사항

## 로그인 및 회원가입

- 사용자가 프로그램을 실행하면 로그인 화면이 나타난다.
- 사용자가 로그인 화면에서 회원가입 버튼을 누르면 회원가입 버튼이 나타난다.
- 사용자가 회원가입 화면에서 회원 정보를 입력하고 회원가입 버튼을 눌렀을 때
  - 중복된 이메일, 유효하지 않은 입력이 있을 시 회원가입 실패 알림이 나타난다.
  - 모든 정보가 적절한 경우 회원정보를 사용자 테이블에 저장하고 회원가입 완료 알림을 표시한다.
- 사용자가 로그인 화면에서 회원 정보를 입력하고 로그인 버튼을 눌렀을 때
  - 사용자 테이블에서 일치하는 회원정보가 없을 경우 로그인 실패 알림을 표시한다.
  - 일치하는 회원 정보가 있을 경우 로그인을 진행하고, 메인 화면을 표시한다.
  
## 상품 목록

- 사용자가 메인 회면의 상품 카테고리 목록에서 상품 카테고리 중 하나를 클릭하면 해당 카테고리에 속한 상품 목록을 표시한다. 상품 카테고리는 상품 카테고리 테이블에 저장하고 관리한다.
- 사용자가 메인 화면의 상품 목록에서 상품 중 하나를 클릭하면 해당 상품이 선택되고, 선택된 상품의 상품명, 가격, 상품설명을 표시한다. 상품은 상품 테이블에 저장/관리한다.
- 사용자가 메인 화면의 모두보기 버튼을 클릭하면 상품 카테고리 목록을 표시한다.
- 사용자가 검색창에 검색어를 입력하고 검색 버튼을 누르면, 상품 테이블에서 상품명에 검색어를 포함하는 모든 상품을 상품 목록에 표시한다.

## 상품 상세정보

- 사용자가 메인 화면에서 상세정보 버튼을 클릭했을 때
  - 현재 선택된 상품이 없으면 알림을 표시한다.
  - 현재 선택된 상품이 있으면 선택된 상품에 대한 상세정보 창을 표시한다.
- 상품 상세정보 창에는 한 종류의 상품에 대한 상품명, 가격, 카테고리, 상품 등록일, 상품 재고, 상품 설명을 표시한다. 리뷰 테이블에 해당 상품에 대한 리뷰가 있으면 추가로 리뷰를 표시한다.
- 사용자가 해당 상품을 장바구니에 담지 않았다면 장바구니에 담기 버튼을, 담았다면 수량 변경 버튼을 표시한다.
- 사용자가 수량 입력 창에 수량을 입력하고
  - 장바구니에 담기 버튼을 클릭하면 "사용자가 이 상품을 장바구니에 몇 개 담았다"는 정보를 장바구니 테이블에 저장한다.
  - 수량 변경 버튼을 클릭하면 장바구니 테이블에서 수량 데이터를 변경한다.

## 장바구니

- 사용자가 메인 화면에서 장바구니 버튼을 클릭하면 장바구니 창을 표시한다.
- 현재 사용자가 장바구니에 담은 상품을 장바구니 테이블에서 가져와서 상품명, 가격, 장바구니에 담은 갯수, 판매 상품의 재고, 삭제 버튼, 수량변경 버튼을 표시하고, 총 금액을 표시한다.
- 사용자가 표시된 각 상품의 삭제 버튼을 클릭하면 표시된 상품을 제거하고, 장바구니 테이블에서도 제거한다.
- 사용자가 표시된 각 상품의 수량변경 버튼을 클릭하면 수량 입력창이 나타난다.
- 수량 입력창에 변경할 수량을 입력하고 확인 버튼을 클릭하면 선택한 장바구니 상품의 "장바구니에 담은 수량"을 현경한다.
- 사용자가 결제하기 버튼을 클릭하면 사용자에게 정말 결제할 것인지 다시 한 번 확인하고, 사용자가 결제를 진행하도록 결정하면 결제를 진행한다. 결제가 진행되면 트랜잭션을 열고, 다음 일련의 과정을 모두 수행한다. 오류 발생 시 모두 취소한다.
  - 주문 번호를 발급받아서 주문 테이블에 주문 데이터를 생성한다.
  - 장바구니 테이블에서 현재 사용자의 장바구니 데이터를 모두 가져온다
  - 가져온 데이터를 주문 상세 테이블로 옮긴다. 이 때, 주문 상세 데이터의 주문 번호를 앞서 발급받았던 주문 번호로 한다.
  - 상품 테이블에서 사용자가 장바구니에 담았던 각 상품의 수량만큼 재고를 감소시킨다.
  - 장바구니 테이블에서 현재 사용자의 장바구니 데이터를 모두 삭제한다.
- 주문 처리가 완료된 후, 배송 테이블에 이 주문에 대한 배송 데이터를 새로 만들어 저장한다.

## 주문내역

- 사용자가 메인 화면에서 주문내역 버튼을 클릭하면 주문내역 창을 표시한다.
- 주문내역 창에는 주문내역 테이블에서 현재 사용자가 진행했던 주문들을 표시하고, 각 주문 내에 포함된 주문 상세내역을 주문 상세 테이블에서 가져와 표시한다.
- 사용자가 각 주문 상세 내역에 관련된 각각의 판매 상품에 대한 리뷰 작성 버튼을 누르면 리뷰 작성 창이 표시된다. 사용자가 1~5 사이의 평점과 리뷰 상세 메시지를 입력하고 리뷰작성 창의 "리뷰 작성" 버튼을 클릭하면, 리뷰 테이블에 현재 사용자가 구매했던 상품에 대한 리뷰를 저장한다.

## 배송정보

- 사용자가 메인 화면에서 배송정보 버튼을 클릭하면 배송정보 창이 나타난다.
- 사용자가 배송정보 창에서 입력창에 배송 번호를 입력하고 검색 버튼을 클릭하면 배송 정보를 표시한다.

## 관리자 계정

사용자는 상품을 구매하는 사용자 이외에 상품 및 상품 재고를 관리하는 관리자 사용자 또한 존재한다. 또한, 주문 완료 후 발급되는 배송에 대해 배송만을 관리하는 사용자를 분리함이 마땅하나, 여기서는 관리자 사용자가 배송정보 또한 관리할 수 있도록 하였다.

- 로그인한 사용자가 관리자 사용자인 경우, 메인 화면에 판매상품 관리, 배송 관리 버튼이 추가로 표시된다.

### 판매상품 관리

- 관리자 사용자가 메인 화면에서 판매상품 관리 버튼을 클릭하면 판매상품 관리 창이 나타난다.
- 판매상품 관리 창에는 현재 존재하는 상품 카테고리 테이블에서 카테고리를 모두 가져와 표시한다.
- 관리자 사용자가 상품 카테고리 중 하나를 클릭하면 해당 카테고리에 속한 모든 상품 목록, 선택한 카테고리의 이름을 변경할 수 있는 이름 입력 창이 나타난다.
  - 관리자 사용자가 상품 카테고리 이름 입력창에 이름을 입력하고, 카테고리 목록의 update 버튼을 클릭하면 현재 선택한 카테고리의 이름을 업데이트하여 카테고리 테이블에 저장한다.
  - 관리자 사용자가 상품 카테고리 목록의 insert 버튼을 클릭하면 새로운 카테고리를 생성하여 카테고리 테이블에 저장한다.
  - 관리자 사용자가 상품 카테고리 목록의 delete 버튼을 클릭하면 현재 선택된 카테고리를 카테고리 테이블에서 삭제한다.
- 관리사 사용자가 상품 중 하나를 클릭하면 해당 상품에 대한 데이터 중 \[상품명, 상품설명, 상품가격, 재고, 이 상품의 카테고리]를 변경할 수 있는 인터페이스가 나타난다.
  - 관리자 사용자가 상품 데이터를 변경하고, 상품 목록의 update 버튼을 클릭하면 현재 선택한 상품 정보를 업데이트하여 상품 테이블에 저장한다.
  - 관리자 사용자가 상품 목록의 insert 버튼을 클릭하면 새로운 상품을 생성하여 상품 테이블에 저장한다.
  - 관리자 사용자가 상품 목록의 delete 버튼을 클릭하면 현재 석택한 상품을 상품 테이블에서 삭제한다.

### 배송 관리

- 관리자 사용자가 메인 화면에서 배송관리 버튼을 클릭하면 배송관리 창이 나타난다.
- 배송관리 창에는 모든 사용자의 배송 데이터, 즉 이 쇼핑몰이 책임져야 할 배송 데이터를 배송 테이블에서 가져와 표시한다.
- 관리자 사용자가 \[배송중, 배송완료, 배송취소] 중 하나를 선택하고, 배송 목록에 나타난 배송 중 하나를 선택하고 상태변경 버튼을 클릭하면 선택한 배송의 상태가 그것으로 업데이트되어 배송 테이블에 저장된다.