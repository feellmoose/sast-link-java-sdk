package fun.feellmoose.enums;

public enum Department {
    Software_R_D(1, "软件研发部"),
    ELECTRONIC(2, "电子部"),
    MULTIMEDIA(3, "多媒体部"),
    OFFICE(21, "办公室"),
    PUBLIC_RELATIONS(22, "外联部"),
    SCIENCE_PROPAGANDA(23, "科宣部"),
    EVENT(24, "赛事部"),
    SAST_STUDIO(25, "SAST工作室"),
    BUREAU(26, "主席团"),
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
