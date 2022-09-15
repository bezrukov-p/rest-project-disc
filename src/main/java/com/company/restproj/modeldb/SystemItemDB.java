package com.company.restproj.modeldb;

import com.company.restproj.model.SystemItemImport;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "items")
public class SystemItemDB {
    @Id
    private String id;
    private String url;
    @Column(name = "parent")
    private String parentId;
    private String type;
    private Long size;
    private Date date;

    public SystemItemDB(SystemItemImport item, Date dateIn){
        id = item.getId();
        url = item.getUrl();
        parentId = item.getParentId();
        type = item.getType();
        size = item.getSize();
        date = dateIn;
    }

    public SystemItemDB() {

    }
}
