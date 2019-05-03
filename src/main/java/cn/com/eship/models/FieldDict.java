package cn.com.eship.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "field_dict", schema = "urm")
public class FieldDict {
    private String id;
    private String dictKey;
    private String dictText;
    private String dictValue;

    @Id
    @Column(name = "id", nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "dict_key", nullable = false, length = 100)
    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    @Basic
    @Column(name = "dict_text", nullable = false, length = 100)
    public String getDictText() {
        return dictText;
    }

    public void setDictText(String dictText) {
        this.dictText = dictText;
    }

    @Basic
    @Column(name = "dict_value", nullable = false, length = 10)
    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDict fieldDict = (FieldDict) o;
        return id == fieldDict.id &&
                Objects.equals(dictKey, fieldDict.dictKey) &&
                Objects.equals(dictText, fieldDict.dictText) &&
                Objects.equals(dictValue, fieldDict.dictValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dictKey, dictText, dictValue);
    }
}
