package com.tcy.mytally.db;

/**
 * 表示收入或者支出具体类型的类
 */
public class TypeBean {
    int id;
    String typeName; // 类型名称
    int imageId;     //未被选中图片Id
    int simageId;    //被选中图片Id
    int kind;        // 收入 ------1   支出 ------0

    public TypeBean(int id, String typeName, int imageId, int simageId, int kind) {
        this.id = id;
        this.typeName = typeName;
        this.imageId = imageId;
        this.simageId = simageId;
        this.kind = kind;
    }

    public TypeBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getSimageId() {
        return simageId;
    }

    public void setSimageId(int simageId) {
        this.simageId = simageId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}
