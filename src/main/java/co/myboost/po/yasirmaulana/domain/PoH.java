package co.myboost.po.yasirmaulana.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Entity
@Table(name = "po_h")
public class PoH {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private LocalDateTime datetime;

    @Column(length = 500)
    private String description;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "total_cost")
    private int totalCost;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;

    @Column(name = "updated_datetime")
    private LocalDateTime updatedDatetime;

    @OneToMany(mappedBy = "poH", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PoD> poDetails;

    @PrePersist
    protected void onCreate() {
        this.createdDatetime = LocalDateTime.now();
        this.createdBy = "Admin"; // sementara hardcode karena belum ada fitur login
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDatetime = LocalDateTime.now();
        this.updatedBy = "Admin"; // sementara hardcode karena belum ada fitur login
    }


}
