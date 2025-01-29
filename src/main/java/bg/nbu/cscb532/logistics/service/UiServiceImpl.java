package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.ResultType;
import org.springframework.stereotype.Service;

@Service("uiService")
public class UiServiceImpl implements UiService {
    @Override
    public String getResultTypeColor(ResultType resultType) {
        if (resultType == ResultType.SUCCESS) {
            return "success";
        }
        if (resultType == ResultType.ERROR) {
            return "danger";
        }
        if (resultType == ResultType.WARNING) {
            return "warning";
        }
        if (resultType == ResultType.INFO) {
            return "info";
        }
        return "secondary";
    }
}
