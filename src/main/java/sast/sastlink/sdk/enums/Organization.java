package sast.sastlink.sdk.enums;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Organization {
    C_SHARP(1,Department.Software_R_D,"C#组"),
    CPP(2,Department.Software_R_D,"C++组"),
    PYTHON(3,Department.Software_R_D,"Python组"),
    LAMBDA(4,Department.Software_R_D,"Lambda组"),
    FRONTEND(5,Department.Software_R_D,"前端组"),
    BACKEND(6,Department.Software_R_D,"后端组"),
    OPS(7,Department.Software_R_D,"运维组"),
    ALGO(8,Department.Software_R_D,"算法组"),
    SECURITY(9,Department.Software_R_D,"安全组"),
    GAME(10,Department.Software_R_D,"游戏组"),

    ONE_CHIP(11,Department.ELECTRONIC,"单片机与控制算法方向"),
    ANALOG_ELECTRONICS(12,Department.ELECTRONIC,"模拟电子与信号方向"),
    IMAGE_PROCESSING(13,Department.ELECTRONIC,"图片处理与嵌入式方向"),
    EDA(14,Department.ELECTRONIC,"EDA方向"),
    FPGA(15,Department.ELECTRONIC,"数字电路与FPGA方向"),

    KINETIC_EFFECT(16,Department.MULTIMEDIA,"动效方向"),
    THREE_D(17,Department.MULTIMEDIA,"三维方向"),
    PHOTO(18,Department.MULTIMEDIA,"摄影与剪辑方向"),
    VISUAL_DESIGN(19,Department.MULTIMEDIA,"视觉设计方向"),
    AUDIO(20,Department.MULTIMEDIA,"音频方向"),

    OFFICE(21,Department.OFFICE,Department.OFFICE.getDep()),
    PUBLIC_RELATIONS(23,Department.PUBLIC_RELATIONS,Department.PUBLIC_RELATIONS.getDep()),
    SCIENCE_PROPAGANDA(24,Department.SCIENCE_PROPAGANDA,Department.SCIENCE_PROPAGANDA.getDep()),
    EVENT(25,Department.EVENT,Department.EVENT.getDep()),
    SAST_STUDIO(26,Department.SAST_STUDIO,Department.SAST_STUDIO.getDep()),
    BUREAU(27,Department.BUREAU,Department.BUREAU.getDep()),

    ;
    private final Integer id;
    private final Department dep;
    private final String org;


    Organization(Integer id, Department dep, String org) {
        this.id = id;
        this.dep = dep;
        this.org = org;
    }

    public Integer getId() {
        return id;
    }

    public Department getDep() {
        return dep;
    }

    public String getOrg() {
        return org;
    }

    private static final Map<Integer,Organization> orgId2Dep = Arrays.stream(values()).collect(Collectors.toMap(Organization::getId,organization -> organization));

    public static Organization getById(Integer id){
        return orgId2Dep.get(id);
    }


}
