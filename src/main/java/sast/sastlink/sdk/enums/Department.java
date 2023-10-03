package sast.sastlink.sdk.enums;

public enum Department {
    Software_R_D(1,"软件研发中心"),
    ELECTRONIC(2,"电子部"),
    MULTIMEDIA(3,"多媒体部"),
    OFFICE(21,"办公室"),
    PUBLIC_RELATIONS(23,"外联部"),
    SCIENCE_PROPAGANDA(24,"科宣部"),
    EVENT(25,"赛事部"),
    SAST_STUDIO(26,"SAST工作室"),
    BUREAU(27,"主席团"),
    ;
    private final Integer id;
    private final String dep;


    Department(Integer id, String dep) {
        this.id = id;
        this.dep = dep;
    }

    public Integer getId() {
        return id;
    }

    public String getDep() {
        return dep;
    }
}
