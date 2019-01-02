package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.kpir.KpirController;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
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

@AllArgsConstructor
@Controller
public class JasperControllerImpl implements JasperController {

    private final KpirController kpirController;

    @Override
    public void compileFile(JasperEnum file) throws JRException {
        InputStream jrxmlInputStream = getClass().getResourceAsStream(file.getJrxmlPath());
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInputStream);
        JRSaver.saveObject(jasperReport, file.getJasperPath());
    }

    @Override
    public void export(String name, Map<String, Object> parameters, OutputStream outputStream) throws JRException {
        JasperEnum jasperFile = JasperEnum.valueOf(name.toUpperCase());
        export(jasperFile, parameters, outputStream);
    }

    @Override
    public void export(JasperEnum jasperFile, Map<String, Object> parameters, OutputStream outputStream) throws JRException {
        JasperReport jasperReport = getJasperReport(jasperFile);
        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList("Invoice"));
        JasperReportsUtils.renderAsPdf(jasperReport, parameters, dataSource, outputStream);
    }

    private JasperReport getJasperReport(JasperEnum jasperFile) throws JRException {
        InputStream jasperInputStream = getClass().getResourceAsStream(jasperFile.getJasperPath());
        return (JasperReport) JRLoader.loadObject(jasperInputStream);
    }

    @Override
    public byte[] generateKpir(BaseUser baseUser, KpirFilterDTO kpirFilterDTO) throws IOException {
        List<KpirDTO> kpirDTOList = kpirController.getAllForPrint(baseUser, kpirFilterDTO);
        Map<String, Object> parameters = prepareParameters(kpirDTOList);

        String prefix = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "-" + baseUser.getCompany().toUpperCase() + "-KPIR";
        File pdfFile = File.createTempFile(prefix, ".pdf");
        OutputStream fileOutputStream = new FileOutputStream(pdfFile);
        try {
            export(JasperEnum.KPIR, parameters, fileOutputStream);
        } catch (JRException e) {
            e.printStackTrace();
        }
        return Files.readAllBytes(pdfFile.toPath());
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

        for (KpirDTO dto: kpirDTOList) {
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
}
