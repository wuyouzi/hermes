package com.ucredit.hermes.model.crawl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.ResultType;

/**
 * 学历信息
 *
 * @author liuxuanyu 2015年11月3日 下午8:16:35
 * @version 1.0
 */
public class UndergraduateCourseInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8704514293558411425L;

    /**
     * 姓名
     */
    private String user_name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 民族
     */
    private String nation;

    /**
     * 出生
     */
    private String birthday;

    /**
     * 身份证
     */
    private String idno;

    /**
     * 考生号
     */
    private String exam_num;

    /**
     * 学号
     */
    private String study_no;

    /**
     * 学校名称
     */
    private String school_name;

    /**
     * 系(所、函授站)
     */
    private String college_name;

    /**
     * 分院
     */
    private String Branch;

    /**
     * 专业名称
     */
    private String majorname;

    /**
     * 班级
     */
    private String class_name;

    /**
     * 层次
     */
    private String degree;

    /**
     * 学制
     */
    private String learn_length;

    /**
     * 学历类别
     */
    private String degree_type;

    /**
     * 学习形式
     */
    private String learn_form_type;

    /**
     * 学籍状态
     */
    private String status;

    /**
     * 入校时间
     */
    private String start_date;

    /**
     * 离校日期
     */
    private String end_date;

    /**
     * 照片路径
     */
    private String photo_file;

    /**
     * 学校信息
     */
    private SchoolInfo schoolInfo;

    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private ResultType resultType;
    private AsyncCode errorCode;
    private String errorMessage;

    /**
     * @return the user_name
     */
    public String getUser_name() {
        return this.user_name;
    }

    /**
     * @param user_name
     *        the user_name to set
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * @param gender
     *        the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the birthday
     */
    public String getBirthday() {
        return this.birthday;
    }

    /**
     * @param birthday
     *        the birthday to set
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdno() {
        return this.idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    /**
     * @return the study_no
     */
    public String getStudy_no() {
        return this.study_no;
    }

    /**
     * @param study_no
     *        the study_no to set
     */
    public void setStudy_no(String study_no) {
        this.study_no = study_no;
    }

    /**
     * @return the school_name
     */
    public String getSchool_name() {
        return this.school_name;
    }

    /**
     * @param school_name
     *        the school_name to set
     */
    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    /**
     * @return the college_name
     */
    public String getCollege_name() {
        return this.college_name;
    }

    /**
     * @param college_name
     *        the college_name to set
     */
    public void setCollege_name(String college_name) {
        this.college_name = college_name;
    }

    /**
     * @return the branch
     */
    public String getBranch() {
        return this.Branch;
    }

    /**
     * @param branch
     *        the branch to set
     */
    public void setBranch(String branch) {
        this.Branch = branch;
    }

    /**
     * @return the majorname
     */
    public String getMajorname() {
        return this.majorname;
    }

    /**
     * @param majorname
     *        the majorname to set
     */
    public void setMajorname(String majorname) {
        this.majorname = majorname;
    }

    /**
     * @return the class_name
     */
    public String getClass_name() {
        return this.class_name;
    }

    /**
     * @param class_name
     *        the class_name to set
     */
    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    /**
     * @return the degree
     */
    public String getDegree() {
        return this.degree;
    }

    /**
     * @param degree
     *        the degree to set
     */
    public void setDegree(String degree) {
        this.degree = degree;
    }

    /**
     * @return the learn_length
     */
    public String getLearn_length() {
        return this.learn_length;
    }

    /**
     * @param learn_length
     *        the learn_length to set
     */
    public void setLearn_length(String learn_length) {
        this.learn_length = learn_length;
    }

    /**
     * @return the degree_type
     */
    public String getDegree_type() {
        return this.degree_type;
    }

    /**
     * @param degree_type
     *        the degree_type to set
     */
    public void setDegree_type(String degree_type) {
        this.degree_type = degree_type;
    }

    /**
     * @return the learn_form_type
     */
    public String getLearn_form_type() {
        return this.learn_form_type;
    }

    /**
     * @param learn_form_type
     *        the learn_form_type to set
     */
    public void setLearn_form_type(String learn_form_type) {
        this.learn_form_type = learn_form_type;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * @param status
     *        the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the start_date
     */
    public String getStart_date() {
        return this.start_date;
    }

    /**
     * @param start_date
     *        the start_date to set
     */
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    /**
     * @return the end_date
     */
    public String getEnd_date() {
        return this.end_date;
    }

    /**
     * @param end_date
     *        the end_date to set
     */
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    /**
     * @return the photo_file
     */
    public String getPhoto_file() {
        return this.photo_file;
    }

    /**
     * @param photo_file
     *        the photo_file to set
     */
    public void setPhoto_file(String photo_file) {
        this.photo_file = photo_file;
    }

    public SchoolInfo getSchoolInfo() {
        return this.schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public ResultType getResultType() {
        return this.resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public AsyncCode getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(AsyncCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getNation() {
        return this.nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getExam_num() {
        return this.exam_num;
    }

    public void setExam_num(String exam_num) {
        this.exam_num = exam_num;
    }

}
