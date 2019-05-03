package cn.com.eship.models.enums;

public enum  BehaviorType {
    CLICK_LAYER("0", "点击层"), EVENT_LAYER("1", "事件层"),SESSION_LAYER("2", "访问层");

    private String value;
    private String name;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private BehaviorType(String value, String name) {
        this.value = value;
        this.name = name;
    }
}
