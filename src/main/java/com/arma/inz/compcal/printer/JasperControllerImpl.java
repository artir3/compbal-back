package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.kpir.KpirController;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUserController;
import com.arma.inz.compcal.users.dto.UserDTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
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

    }

    private JasperReport getJasperReport(JasperEnum jasperFile) throws JRException {
        InputStream jasperInputStream = getClass().getResourceAsStream(jasperFile.getJasperPath());
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperInputStream);
        return jasperReport;
    }

    @Override
    public byte[] generateKpir(String authorization, KpirFilterDTO kpirFilterDTO) throws IOException {
        UserDTO baseUser = baseUserController.getUserDTO(authorization.substring(5));
        List<KpirDTO> kpirDTOList = kpirController.getListByFilter(authorization.substring(5), kpirFilterDTO);
        Map<String, Object> parameters = new HashMap<>();

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
}
