package com.bezrukov.restproj.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "itemshistory")
public class SystemItemHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNumber;
    private String id;
    private String url;
    @Column(name = "parent")
    private String parentId;
    private String type;
    private Long size;
    private LocalDateTime date;

    public SystemItemHistoryEntity(SystemItemEntity item) {
        id = item.getId();
        url = item.getUrl();
        parentId = item.getParentId();
        type = item.getType();
        size = item.getSize();
        date = item.getDate();
    }
}
