package org.hpin.common.util;

public interface StaticVariable {

  /**
   * @see ..字典类型 FAULT_CLASS 紧急程度（比如：次要/一般/重要/紧急/来自领导投诉）
   */
  public static final int FAULTCLASS_CY = 1;
  public static final int FAULTCLASS_YB = 2;
  public static final int FAULTCLASS_ZY = 3;
  public static final int FAULTCLASS_JJ = 4;
  public static final int FAULTCLASS_LZLDTS = 5;

  //added by jerry
  //oper type
  public static final int OPER_SEND = 1;
  public static final int OPER_AUDIT = 3;
  public static final int OPER_COPY = 2;

  //worksheet flow config
  public static final int OBJECT_DEPT = 1;
  public static final int OBJECT_USER = 2;
  public static final int OBJECT_GROUP = 3;

  /**
   * @see ..催办方式--短信。
   */
  public static final int HTYPE_SMS = 0;



  /**
   * @see ..短信是否可用。
   */
  public static final String  SendSMsgAvailable = "true";
  public static final String  SendSMsgNotAvailable = "false";


  /**
   * @see ..数据库类型。
   */
  public static final String  ORACLE = "oracle";
  public static final String  INFORMIX = "informix";

  /**
   * @see ..编码类型。
   */
  public static final String GB2312 = "GB2312";
  public static final String ISO= "ISO-8859-1";

  /**
   * @see ..操作类型，用于日至管理。
   */
  public static final String ERROR = "error";
  public static final String OPER = "oper";
  public static final String INFOR = "infor";
  public static final String WARN = "warn";

  /**
   * @see ..删除标记
   */
  public static final int DELETED = 1;

  /**
   * @see ..未删除标记
   */
  public static final int UNDELETED = 0;

  /**
   * @see ..隐藏标记
   */
  public static final int HIDE=1;

  /**
   * @see ..不隐藏标记
   */
  public static final int UNHIDE=0;

  /**
   * @see ..字典管理的节点值
   */
  public static final int DICTROOT=1216;

  /**
   * @see ..树形展示是的叶子标记
   */
  public static final int LEAF=1;

  public static final int NOTLEAF=0;

  public static final String STRLEAF = "1";

  /**
   * @see ..系统默认的空值
   */
  public static final String defaultnull = "-10";
  public static final int defnull= -10;

  /**
   * @see ..字典类型 FAULT_CLASS 紧急程度（比如：一般/次要/重要/紧急）
   * @see ..根据数据库定义配置此值
   */
  public static final int FAULTCLASS = 2;

  /**
   * @see ..字典类型 ATISFACTION 满意程度（比如：满意/不满意）
   * @see ..根据数据库定义配置此值
   */
  public static final int SATISFACTION = 3;


  /**
   * @see ..默认整型错误码
   */
  public static final int INTERRORCODE = -10;
  /**
   * @see ..认字符串型错误码
   */
  public static final String STRERRORCODE = "-10";



}

