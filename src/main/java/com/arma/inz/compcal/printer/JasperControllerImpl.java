package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.kpir.KpirController;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import lombok.extern.java.Log;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.jasperreports.JasperReportsUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
@Controller
public class JasperControllerImpl implements JasperController {

    private final KpirController kpirController;
    private final JasperMailSender jasperMailSender;
    private File generatedPdf;

    public JasperControllerImpl(KpirController kpirController, JasperMailSender jasperMailSender) {
        this.kpirController = kpirController;
        this.jasperMailSender = jasperMailSender;
        generatedPdf = null;
    }

    @Override
    public void compileFile(JasperEnum file) {
        try {
            InputStream jrxmlInputStream = getClass().getResourceAsStream(file.getJrxmlPath());
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInputStream);
            JRSaver.saveObject(jasperReport, file.getJasperPath());
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resource getKpirResource(BaseUser baseUser, KpirFilterDTO kpirFilterDTO) {
        Resource urlResource = null;
        try {
            generateKpir(baseUser, kpirFilterDTO);
            urlResource = new UrlResource(generatedPdf.toURI());
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
        return urlResource;
    }

    @Override
    public byte[] getKpirBytes(BaseUser baseUser, KpirFilterDTO kpirFilterDTO) {
        byte[] bytes = new byte[0];
        try {
            generateKpir(baseUser, kpirFilterDTO);
            bytes = Files.readAllBytes(generatedPdf.toPath());
            deletePdfFile();
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
        return bytes;
    }


    @Override
    public File getKpirFile(BaseUser baseUser, KpirFilterDTO kpirFilterDTO) {
        try {
            generateKpir(baseUser, kpirFilterDTO);
        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
        return generatedPdf;
    }

    @Override
    public void sendKpirPdftoMail(BaseUser baseUser, KpirFilterDTO kpirFilterDTO) {
        try {
            generateKpir(baseUser, kpirFilterDTO);
        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
        jasperMailSender.sendKpirEmailWithFile(baseUser, generatedPdf);
        deletePdfFile();
    }

    private void export(String name, Map<String, Object> parameters, OutputStream outputStream) throws JRException {
        JasperEnum jasperFile = JasperEnum.valueOf(name.toUpperCase());
        export(jasperFile, parameters, outputStream);
    }

    private void export(JasperEnum jasperFile, Map<String, Object> parameters, OutputStream outputStream) throws JRException {
        JasperReport jasperReport = getJasperReport(jasperFile);
        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList("Invoice"));
        JasperReportsUtils.renderAsPdf(jasperReport, parameters, dataSource, outputStream);
    }

    private JasperReport getJasperReport(JasperEnum jasperFile) throws JRException {
        InputStream jasperInputStream = getClass().getResourceAsStream(jasperFile.getJasperPath());
        return (JasperReport) JRLoader.loadObject(jasperInputStream);
    }

    private void generateKpir(BaseUser baseUser, KpirFilterDTO kpirFilterDTO) throws JRException, IOException {
        List<KpirDTO> kpirDTOList = kpirController.getAllForPrint(baseUser, kpirFilterDTO);
        Map<String, Object> parameters = prepareParameters(kpirDTOList);
        deletePdfFile();
        String prefix = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "-" + baseUser.getCompany().toUpperCase() + "-KPIR";
        generatedPdf = File.createTempFile(prefix, ".pdf");
        OutputStream fileOutputStream = new FileOutputStream(generatedPdf);
        export(JasperEnum.KPIR, parameters, fileOutputStream);
    }

    private Map<String, Object> prepareParameters(List<KpirDTO> kpirDTOList) {
        Map<String, Object> parameters = new HashMap<>();
        BigDecimal totalSoldIncome = BigDecimal.ZERO;
        BigDecimal totalOtherIncome = BigDecimal.ZERO;
        BigDecimal totalAllIncome = BigDecimal.ZERO;
        BigDecimal totalPurchaseCosts = BigDecimal.ZERO;
        BigDecimal totalPurchaseSideCosts = BigDecimal.ZERO;
        BigDecimal totalPaymentCost = BigDecimal.ZERO;
        BigDecimal totalOtherCosts = BigDecimal.ZERO;
        BigDecimal totalSumCosts = BigDecimal.ZERO;
        BigDecimal totalOther = BigDecimal.ZERO;
        BigDecimal totalRadCosts = BigDecimal.ZERO;

        for (KpirDTO dto : kpirDTOList) {
            totalSoldIncome = totalSoldIncome.add(getNotNullBigDecimal(dto.getSoldIncome()));
            totalOtherIncome = totalOtherIncome.add(getNotNullBigDecimal(dto.getOtherIncome()));
            totalAllIncome = totalAllIncome.add(getNotNullBigDecimal(dto.getAllIncome()));
            totalPurchaseCosts = totalPurchaseCosts.add(getNotNullBigDecimal(dto.getPurchaseCosts()));
            totalPurchaseSideCosts = totalPurchaseSideCosts.add(getNotNullBigDecimal(dto.getPurchaseSideCosts()));
            totalPaymentCost = totalPaymentCost.add(getNotNullBigDecimal(dto.getPaymentCost()));
            totalOtherCosts = totalOtherCosts.add(getNotNullBigDecimal(dto.getOtherCosts()));
            totalSumCosts = totalSumCosts.add(getNotNullBigDecimal(dto.getSumCosts()));
            totalOther = totalOther.add(getNotNullBigDecimal(dto.getOther()));
            totalRadCosts = totalRadCosts.add(getNotNullBigDecimal(dto.getRadCosts()));
        }
        parameters.put("totalSoldIncome", totalSoldIncome);
        parameters.put("totalOtherIncome", totalOtherIncome);
        parameters.put("totalAllIncome", totalAllIncome);
        parameters.put("totalPurchaseCosts", totalPurchaseCosts);
        parameters.put("totalPurchaseSideCosts", totalPurchaseSideCosts);
        parameters.put("totalPaymentCost", totalPaymentCost);
        parameters.put("totalOtherCosts", totalOtherCosts);
        parameters.put("totalSumCosts", totalSumCosts);
        parameters.put("totalOther", totalOther);
        parameters.put("totalRadCosts", totalRadCosts);

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(kpirDTOList);
        parameters.put("datasource", ds);
        return parameters;
    }

    private BigDecimal getNotNullBigDecimal(BigDecimal soldIncome) {
        return soldIncome == null ? BigDecimal.ZERO : soldIncome;
    }

    private void deletePdfFile() {
        try {
            if (generatedPdf != null) {
                if (Files.exists(generatedPdf.toPath())) {
                    Files.delete(generatedPdf.toPath());
                }
                generatedPdf = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
