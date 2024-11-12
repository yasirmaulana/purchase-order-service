package co.myboost.po.yasirmaulana.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 500)
    private String name;

    @Column(length = 500)
    private String description;

    @Column
    private int price;

    @Column
    private int cost;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;

    @Column(name = "updated_datetime")
    private LocalDateTime updatedDatetime;

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
