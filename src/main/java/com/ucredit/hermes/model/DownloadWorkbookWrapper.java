/**
 * Copyright(c) 2011-2012 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author ijay
 */
public class DownloadWorkbookWrapper {
    private String fileName;
    /**
     * 2003 版本Excel
     */
    private HSSFWorkbook workbook;
    /**
     * 2007+ 版本Excel
     */
    private XSSFWorkbook XSSFWorkbook;

    /**
     * @param fileName
     * @param workbook
     */
    public DownloadWorkbookWrapper(String fileName, HSSFWorkbook workbook) {

        this.fileName = fileName;
        this.workbook = workbook;
    }

    /**
     * @param fileName
     * @param workbook
     */
    public DownloadWorkbookWrapper(String fileName, XSSFWorkbook workbook) {

        this.fileName = fileName;
        this.XSSFWorkbook = workbook;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * @param fileName
     *        the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the workbook
     */
    public HSSFWorkbook getWorkbook() {
        return this.workbook;
    }

    /**
     * @param workbook
     *        the workbook to set
     */
    public void setWorkbook(HSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    /**
     * @return the xSSFWorkbook
     */
    public XSSFWorkbook getXSSFWorkbook() {
        return this.XSSFWorkbook;
    }

    /**
     * @param xSSFWorkbook
     *        the xSSFWorkbook to set
     */
    public void setXSSFWorkbook(XSSFWorkbook xSSFWorkbook) {
        this.XSSFWorkbook = xSSFWorkbook;
    }

}
