package co.myboost.po.yasirmaulana.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "po_d")
public class PoD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "poh_id")
    private PoH poH;

    @Column(name = "item_id")
    private int itemId;

    @Column(name = "item_qty")
    private int itemQty;

    @Column(name = "item_cost")
    private int itemCost;

    @Column(name = "item_price")
    private int itemPrice;
}
