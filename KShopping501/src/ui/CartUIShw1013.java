package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import dao.CartDAOShw1013;
import dao.ShippingDAO;
import dto.CartDTOShw1013;
import dto.UserDTO;
import simulate.Carrier;

public class CartUIShw1013 extends JFrame {

    private CartDAOShw1013 cartDAOShw1013;
    private UserDTO currentUser;
    private List<CartDTOShw1013> products;
    private JLabel totalLabel;

    private int total = 0;

    public CartUIShw1013(UserDTO user) {
        currentUser = user;
        setTitle("장바구니");
        setSize(600, 600);
        setLocationRelativeTo(null);

        cartDAOShw1013 = new CartDAOShw1013();
        // products = cartDAOShw1013.getAllCartItems();
        products = cartDAOShw1013.getCartItems(currentUser.getUserId());

        totalLabel = new JLabel("총 구매 금액: 0원");

        String[] columnNames = {"상품명", "가격", "개수", "재고", "삭제", "수량 수정"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // DB에서 가져온 데이터로 테이블 모델 채우기
        for (CartDTOShw1013 product : products) {
            model.addRow(new Object[]{
                product.getProductName(),  // 상품명
                product.getPrice(),        // 가격
                product.getQuantity(),     // 개수
                product.getStock(),        // 재고
                "삭제",                     // 삭제 버튼
                "수량 수정"                 // 수량 수정 버튼
            });
        }

        JTable table = new JTable(model);

        // 상품명 가운데 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // 상품명
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // 개수
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // 재고

        // 가격 우측 정렬
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        rightRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Number) {
                    setText(NumberFormat.getNumberInstance(Locale.KOREA).format(value) + " 원");
                }
                return c;
            }
        };
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer); // 가격

        // Set row height
        table.setRowHeight((int) (table.getRowHeight() * 1.5));

        // 버튼 렌더러 및 에디터 추가
        table.getColumn("삭제").setCellRenderer(new ButtonRenderer());
        table.getColumn("삭제").setCellEditor(new ButtonEditor(new JCheckBox(), model, "삭제"));

        table.getColumn("수량 수정").setCellRenderer(new ButtonRenderer());
        table.getColumn("수량 수정").setCellEditor(new ButtonEditor(new JCheckBox(), model, "수량 수정"));

        JScrollPane scrollPane = new JScrollPane(table);

        JButton checkoutButton = new JButton("결제하기");
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 결제 처리
                int confirmation = JOptionPane.showConfirmDialog(CartUIShw1013.this, "결제를 진행하시겠습니까?", "결제 확인", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    // cartDAOShw1013.updateStockAfterPayment(currentUser.getUserId());
                    Integer orderId = cartDAOShw1013.order(currentUser.getUserId(), CartUIShw1013.this.total);
                    JOptionPane.showMessageDialog(CartUIShw1013.this, "결제가 완료되었습니다.");

                    ShippingDAO shippingDAO = new ShippingDAO();
                    shippingDAO.addShipping(orderId.intValue(), UUID.randomUUID().toString(), Carrier.getCarrier(), "배송중");
                    
                    // 장바구니 비우기
                    products.clear();
                    model.setRowCount(0); // 테이블 초기화
                    updateTotalPrice(model, totalLabel);
                }
            }
        });

        updateTotalPrice(model, totalLabel);

        model.addTableModelListener(e -> updateTotalPrice(model, totalLabel));

        JPanel panel = new JPanel();
        panel.add(checkoutButton);
        panel.add(totalLabel);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }

    private void updateTotalPrice(DefaultTableModel model, JLabel totalLabel) {
        int total = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                Object priceObj = model.getValueAt(i, 1);
                Object quantityObj = model.getValueAt(i, 2);

                int price = (priceObj instanceof Number) ? ((Number) priceObj).intValue() : Integer.parseInt(priceObj.toString());
                int quantity = (quantityObj instanceof Number) ? ((Number) quantityObj).intValue() : Integer.parseInt(quantityObj.toString());

                total += price * quantity;

            } catch (NumberFormatException | ClassCastException e) {
                System.err.println("Error parsing price or quantity at row " + i + ": " + e.getMessage());
            }
        }

        totalLabel.setText("총 구매 금액: " + NumberFormat.getNumberInstance(Locale.KOREA).format(total) + " 원");
        this.total = total;
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private DefaultTableModel model;
        private int row;
        private String actionType;

        public ButtonEditor(JCheckBox checkBox, DefaultTableModel model, String actionType) {
            super(checkBox);
            this.model = model;
            this.actionType = actionType;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            label = (value == null) ? "" : value.toString();
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CartDTOShw1013 product = products.get(row);

                    if ("삭제".equals(actionType)) {
                        cartDAOShw1013.deleteCartItem(product.getCartId());
                        model.removeRow(ButtonEditor.this.row);
                        products.remove(row);
                    } else if ("수량 수정".equals(actionType)) {
                        String newQuantityStr = JOptionPane.showInputDialog(CartUIShw1013.this,
                            "새로운 수량을 입력하세요:", product.getQuantity());
                        try {
                            int newQuantity = Integer.parseInt(newQuantityStr);
                            if (newQuantity > 0 && newQuantity <= product.getStock()) {
                                cartDAOShw1013.updateCartItem(product.getCartId(), newQuantity);
                                model.setValueAt(newQuantity, row, 2);
                                product.setQuantity(newQuantity);
                            } else {
                                JOptionPane.showMessageDialog(CartUIShw1013.this, "유효한 수량을 입력하세요.");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(CartUIShw1013.this, "숫자를 입력하세요.");
                        }
                    }
                }
            });
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }
}
