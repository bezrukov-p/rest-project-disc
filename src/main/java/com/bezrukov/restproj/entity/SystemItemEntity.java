package com.bezrukov.restproj.entity;

import com.bezrukov.restproj.dto.SystemItemImport;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "items")
@NoArgsConstructor
public class SystemItemEntity {
    @Id
    private String id;
    private String url;
    @Column(name = "parent")
    private String parentId;
    private String type;
    private Long size;
    private LocalDateTime date;

    public SystemItemEntity(SystemItemImport item, LocalDateTime dateIn){
        id = item.getId();
        url = item.getUrl();
        parentId = item.getParentId();
        type = item.getType();
        size = item.getSize();
        date = dateIn;
    }
}
