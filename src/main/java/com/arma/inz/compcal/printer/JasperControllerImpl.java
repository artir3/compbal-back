package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.AuthorizationHeaderUtils;
import com.arma.inz.compcal.kpir.KpirController;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import com.arma.inz.compcal.users.BaseUserController;
import com.arma.inz.compcal.users.dto.UserDTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.jasperreports.JasperReportsUtils;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class JasperControllerImpl implements JasperController {

    @Autowired
    private BaseUserController baseUserController;

    @Autowired
    private KpirController kpirController;

    @Autowired
    private AuthorizationHeaderUtils header;

    @Autowired
    private JasperController jasperController;

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
//        JasperExportManager.exportReportToPdfFile(jasperReport, "src/main/resources/table.pdf");
    }

    private JasperReport getJasperReport(JasperEnum jasperFile) throws JRException {
        InputStream jasperInputStream = getClass().getResourceAsStream(jasperFile.getJasperPath());
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperInputStream);
        return jasperReport;
    }

    @Override
    public byte[] generateKpir(BaseUser baseUser, KpirFilterDTO kpirFilterDTO) throws IOException {
        kpirFilterDTO.setType(null);
        List<KpirDTO> kpirDTOList = kpirController.getAll(baseUser, kpirFilterDTO);
        Map<String, Object> parameters = new HashMap<>();
        prepareParameters(parameters, kpirDTOList);

        String prefix = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "-" + baseUser.getCompany().toUpperCase() + "-KPIR";
        File pdfFile = File.createTempFile(prefix, ".pdf");
        pdfFile = new File("/Users/arma/Desktop/"+prefix+".pdf");
        OutputStream fileOutputStream = new FileOutputStream(pdfFile);
        try {
            export(JasperEnum.KPIR, parameters, fileOutputStream);
        } catch (JRException e) {
            e.printStackTrace();
        }
        return Files.readAllBytes(pdfFile.toPath());
    }

    private void prepareParameters(Map<String, Object> parameters, List<KpirDTO> kpirDTOList) {
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
            totalSoldIncome = totalSoldIncome.add(dto.getSoldIncome() == null ? BigDecimal.ZERO : dto.getSoldIncome());
            totalOtherIncome = totalOtherIncome.add(dto.getOtherIncome() == null ? BigDecimal.ZERO : dto.getOtherIncome());
            totalAllIncome = totalAllIncome.add(dto.getAllIncome() == null ? BigDecimal.ZERO : dto.getAllIncome());
            totalPurchaseCosts = totalPurchaseCosts.add(dto.getPurchaseCosts() == null ? BigDecimal.ZERO : dto.getPurchaseCosts());
            totalPurchaseSideCosts = totalPurchaseSideCosts.add(dto.getPurchaseSideCosts() == null ? BigDecimal.ZERO : dto.getPurchaseSideCosts());
            totalPaymentCost = totalPaymentCost.add(dto.getPaymentCost() == null ? BigDecimal.ZERO : dto.getPaymentCost());
            totalOtherCosts = totalOtherCosts.add(dto.getOtherCosts() == null ? BigDecimal.ZERO : dto.getOtherCosts());
            totalSumCosts = totalSumCosts.add(dto.getSumCosts() == null ? BigDecimal.ZERO : dto.getSumCosts());
            totalOther = totalOther.add(dto.getOther() == null ? BigDecimal.ZERO : dto.getOther());
            totalRadCosts = totalRadCosts.add(dto.getRadCosts() == null ? BigDecimal.ZERO : dto.getRadCosts());
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

    }
}
