package com.ucredit.hermes.model.pengyuan;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 教育学历信息
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "degree_college_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DegreeCollegeInfo extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 770793543712925161L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 毕业院校
     */
    @Column(length = 64)
    private String college;

    /**
     * 学历证编号
     */
    @Column(length = 32)
    private String levelNo;

    /**
     * 入学时间，格式YYYYMMDD
     */
    @Temporal(TemporalType.DATE)
    private Date startTime;

    /**
     * 毕业时间，格式YYYY
     */
    @Column(length = 32)
    private String graduateTime;

    /**
     * 学习形式，如：普通全日制、函授、脱产、网络教育、业余、夜大学、在职、不详
     */
    @Column(length = 32)
    private String studyStyle;

    /**
     * 学历类别，如：研究生、成人、普通、网络教育、自学考试
     */
    @Column(length = 32)
    private String studyType;

    /**
     * 所学专业
     */
    @Column(length = 32)
    private String specialty;

    /**
     * 是否国家重点学科
     */
    @Column(length = 32)
    private String isKeySubject;

    /**
     * 学历层次，如：博士研究生、硕士研究生、本科、专科、第二学士学位、第二专科、专科
     */
    @Column(length = 32)
    private String degree;

    /**
     * 毕业结论，毕业、结业、肄业之一
     */
    @Column(length = 32)
    private String studyResult;

    /**
     * 相片BASE64编码
     */
    @Lob
    private String photo;

    /**
     * 相片格式：目前固定为JPG
     */
    @Column(length = 32)
    private String photoStyle;

    /**
     * 毕业院校原名称
     */
    @Column(length = 64)
    private String collegeOldName;

    /**
     * 院校所在地
     */
    @Column(length = 64)
    private String address;

    /**
     * 创建时间
     */

    private String createDate;

    /**
     * 创建年限
     */
    @Column(length = 32)
    private Integer createYears;

    /**
     * 学校性质
     */
    @Column(length = 64)
    private String colgCharacter;

    /**
     * 办学层次
     */
    @Column(length = 32)
    private String colgLevel;

    /**
     * 办学性质
     */
    @Column(length = 64)
    private String character;

    /**
     * 学校类别
     */
    @Column(length = 64)
    private String colgType;

    /**
     * 理科录取批次
     */
    @Column(length = 64)
    private String scienceBatch;

    /**
     * 文科录取批次
     */
    @Column(length = 64)
    private String artBatch;

    /**
     * 博士后流动站数
     */
    @Column(length = 32)
    private Integer postDoctorNum;

    /**
     * 博士点个数
     */
    @Column(length = 32)
    private Integer doctorDegreeNum;

    /**
     * 硕士点个数
     */
    @Column(length = 32)
    private Integer masterDegreeNum;

    /**
     * 院士人数
     */
    @Column(length = 32)
    private Integer academicianNum;

    /**
     * 是否211院校
     */
    @Column(length = 32)
    private String is211;

    /**
     * 主管教育部门
     */
    @Column(length = 32)
    private String manageDept;

    /**
     * 国家重点学科数
     */
    @Column(length = 32)
    private Integer keySubjectNum;

    /*
     * @ManyToOne(optional = false)
     * @JoinColumn(name = "educationInfos_id")
     * private EducationInfo educationInfos;
     */

    private Integer educationInfos_id;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCollege() {
        return this.college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCollegeOldName() {
        return this.collegeOldName;
    }

    public void setCollegeOldName(String collegeOldName) {
        this.collegeOldName = collegeOldName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getCreateYears() {
        return this.createYears;
    }

    public void setCreateYears(Integer createYears) {
        this.createYears = createYears;
    }

    public String getColgCharacter() {
        return this.colgCharacter;
    }

    public void setColgCharacter(String colgCharacter) {
        this.colgCharacter = colgCharacter;
    }

    public String getColgLevel() {
        return this.colgLevel;
    }

    public void setColgLevel(String colgLevel) {
        this.colgLevel = colgLevel;
    }

    public String getCharacter() {
        return this.character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getColgType() {
        return this.colgType;
    }

    public void setColgType(String colgType) {
        this.colgType = colgType;
    }

    public String getScienceBatch() {
        return this.scienceBatch;
    }

    public void setScienceBatch(String scienceBatch) {
        this.scienceBatch = scienceBatch;
    }

    public String getArtBatch() {
        return this.artBatch;
    }

    public void setArtBatch(String artBatch) {
        this.artBatch = artBatch;
    }

    public Integer getPostDoctorNum() {
        return this.postDoctorNum;
    }

    public void setPostDoctorNum(Integer postDoctorNum) {
        this.postDoctorNum = postDoctorNum;
    }

    public Integer getDoctorDegreeNum() {
        return this.doctorDegreeNum;
    }

    public void setDoctorDegreeNum(Integer doctorDegreeNum) {
        this.doctorDegreeNum = doctorDegreeNum;
    }

    public Integer getMasterDegreeNum() {
        return this.masterDegreeNum;
    }

    public void setMasterDegreeNum(Integer masterDegreeNum) {
        this.masterDegreeNum = masterDegreeNum;
    }

    public Integer getAcademicianNum() {
        return this.academicianNum;
    }

    public void setAcademicianNum(Integer academicianNum) {
        this.academicianNum = academicianNum;
    }

    public String getIs211() {
        return this.is211;
    }

    public void setIs211(String is211) {
        this.is211 = is211;
    }

    public String getManageDept() {
        return this.manageDept;
    }

    public void setManageDept(String manageDept) {
        this.manageDept = manageDept;
    }

    public Integer getKeySubjectNum() {
        return this.keySubjectNum;
    }

    public void setKeySubjectNum(Integer keySubjectNum) {
        this.keySubjectNum = keySubjectNum;
    }

    public String getLevelNo() {
        return this.levelNo;
    }

    public void setLevelNo(String levelNo) {
        this.levelNo = levelNo;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getGraduateTime() {
        return this.graduateTime;
    }

    public void setGraduateTime(String graduateTime) {
        this.graduateTime = graduateTime;
    }

    public String getStudyStyle() {
        return this.studyStyle;
    }

    public void setStudyStyle(String studyStyle) {
        this.studyStyle = studyStyle;
    }

    public String getStudyType() {
        return this.studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public String getSpecialty() {
        return this.specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getIsKeySubject() {
        return this.isKeySubject;
    }

    public void setIsKeySubject(String isKeySubject) {
        this.isKeySubject = isKeySubject;
    }

    public String getDegree() {
        return this.degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getStudyResult() {
        return this.studyResult;
    }

    public void setStudyResult(String studyResult) {
        this.studyResult = studyResult;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoStyle() {
        return this.photoStyle;
    }

    public void setPhotoStyle(String photoStyle) {
        this.photoStyle = photoStyle;
    }

    public Integer getEducationInfos_id() {
        return this.educationInfos_id;
    }

    public void setEducationInfos_id(Integer educationInfos_id) {
        this.educationInfos_id = educationInfos_id;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + (this.artBatch == null ? 0 : this.artBatch.hashCode());
        result = prime * result
                + (this.colgLevel == null ? 0 : this.colgLevel.hashCode());
        result = prime * result
                + (this.colgType == null ? 0 : this.colgType.hashCode());
        result = prime * result
            + (this.college == null ? 0 : this.college.hashCode());
        result = prime
            * result
                + (this.collegeOldName == null ? 0 : this.collegeOldName.hashCode());
        result = prime * result
            + (this.degree == null ? 0 : this.degree.hashCode());
        result = prime
            * result
                + (this.educationInfos_id == null ? 0 : this.educationInfos_id
                .hashCode());
        result = prime * result
                + (this.graduateTime == null ? 0 : this.graduateTime.hashCode());
        result = prime * result + (this.id == null ? 0 : this.id.hashCode());
        result = prime * result
            + (this.is211 == null ? 0 : this.is211.hashCode());
        result = prime * result
                + (this.isKeySubject == null ? 0 : this.isKeySubject.hashCode());
        result = prime * result
                + (this.keySubjectNum == null ? 0 : this.keySubjectNum.hashCode());
        result = prime * result
            + (this.levelNo == null ? 0 : this.levelNo.hashCode());
        result = prime * result
                + (this.manageDept == null ? 0 : this.manageDept.hashCode());
        result = prime
            * result
                + (this.masterDegreeNum == null ? 0 : this.masterDegreeNum
                .hashCode());
        result = prime * result
            + (this.photo == null ? 0 : this.photo.hashCode());
        result = prime * result
                + (this.photoStyle == null ? 0 : this.photoStyle.hashCode());
        result = prime * result
                + (this.postDoctorNum == null ? 0 : this.postDoctorNum.hashCode());
        result = prime * result
                + (this.scienceBatch == null ? 0 : this.scienceBatch.hashCode());
        result = prime * result
                + (this.specialty == null ? 0 : this.specialty.hashCode());
        result = prime * result
                + (this.startTime == null ? 0 : this.startTime.hashCode());
        result = prime * result
                + (this.studyResult == null ? 0 : this.studyResult.hashCode());
        result = prime * result
                + (this.studyStyle == null ? 0 : this.studyStyle.hashCode());
        result = prime * result
                + (this.studyType == null ? 0 : this.studyType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        DegreeCollegeInfo other = (DegreeCollegeInfo) obj;
        if (this.artBatch == null) {
            if (other.artBatch != null) {
                return false;
            }
        } else if (!this.artBatch.equals(other.artBatch)) {
            return false;
        }
        if (this.colgLevel == null) {
            if (other.colgLevel != null) {
                return false;
            }
        } else if (!this.colgLevel.equals(other.colgLevel)) {
            return false;
        }
        if (this.colgType == null) {
            if (other.colgType != null) {
                return false;
            }
        } else if (!this.colgType.equals(other.colgType)) {
            return false;
        }
        if (this.college == null) {
            if (other.college != null) {
                return false;
            }
        } else if (!this.college.equals(other.college)) {
            return false;
        }
        if (this.collegeOldName == null) {
            if (other.collegeOldName != null) {
                return false;
            }
        } else if (!this.collegeOldName.equals(other.collegeOldName)) {
            return false;
        }
        if (this.degree == null) {
            if (other.degree != null) {
                return false;
            }
        } else if (!this.degree.equals(other.degree)) {
            return false;
        }
        if (this.educationInfos_id == null) {
            if (other.educationInfos_id != null) {
                return false;
            }
        } else if (!this.educationInfos_id.equals(other.educationInfos_id)) {
            return false;
        }
        if (this.graduateTime == null) {
            if (other.graduateTime != null) {
                return false;
            }
        } else if (!this.graduateTime.equals(other.graduateTime)) {
            return false;
        }
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.is211 == null) {
            if (other.is211 != null) {
                return false;
            }
        } else if (!this.is211.equals(other.is211)) {
            return false;
        }
        if (this.isKeySubject == null) {
            if (other.isKeySubject != null) {
                return false;
            }
        } else if (!this.isKeySubject.equals(other.isKeySubject)) {
            return false;
        }
        if (this.keySubjectNum == null) {
            if (other.keySubjectNum != null) {
                return false;
            }
        } else if (!this.keySubjectNum.equals(other.keySubjectNum)) {
            return false;
        }
        if (this.levelNo == null) {
            if (other.levelNo != null) {
                return false;
            }
        } else if (!this.levelNo.equals(other.levelNo)) {
            return false;
        }
        if (this.manageDept == null) {
            if (other.manageDept != null) {
                return false;
            }
        } else if (!this.manageDept.equals(other.manageDept)) {
            return false;
        }
        if (this.masterDegreeNum == null) {
            if (other.masterDegreeNum != null) {
                return false;
            }
        } else if (!this.masterDegreeNum.equals(other.masterDegreeNum)) {
            return false;
        }
        if (this.photo == null) {
            if (other.photo != null) {
                return false;
            }
        } else if (!this.photo.equals(other.photo)) {
            return false;
        }
        if (this.photoStyle == null) {
            if (other.photoStyle != null) {
                return false;
            }
        } else if (!this.photoStyle.equals(other.photoStyle)) {
            return false;
        }
        if (this.postDoctorNum == null) {
            if (other.postDoctorNum != null) {
                return false;
            }
        } else if (!this.postDoctorNum.equals(other.postDoctorNum)) {
            return false;
        }
        if (this.scienceBatch == null) {
            if (other.scienceBatch != null) {
                return false;
            }
        } else if (!this.scienceBatch.equals(other.scienceBatch)) {
            return false;
        }
        if (this.specialty == null) {
            if (other.specialty != null) {
                return false;
            }
        } else if (!this.specialty.equals(other.specialty)) {
            return false;
        }
        if (this.startTime == null) {
            if (other.startTime != null) {
                return false;
            }
        } else if (!this.startTime.equals(other.startTime)) {
            return false;
        }
        if (this.studyResult == null) {
            if (other.studyResult != null) {
                return false;
            }
        } else if (!this.studyResult.equals(other.studyResult)) {
            return false;
        }
        if (this.studyStyle == null) {
            if (other.studyStyle != null) {
                return false;
            }
        } else if (!this.studyStyle.equals(other.studyStyle)) {
            return false;
        }
        if (this.studyType == null) {
            if (other.studyType != null) {
                return false;
            }
        } else if (!this.studyType.equals(other.studyType)) {
            return false;
        }
        return true;
    }

}
