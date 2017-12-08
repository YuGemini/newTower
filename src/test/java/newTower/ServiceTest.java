package newTower;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.vastio.rest.MainInitializer;
import com.vastio.rest.entity.StationInfo;
import com.vastio.rest.service.StationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created By zhaotf on 2017/11/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MainInitializer.class})
@ActiveProfiles("prod")
public class ServiceTest {
    @Autowired
    StationService stationService;

    @Test
    public void hitRateSvc() {
        List<StationInfo> stationInfos = new ArrayList<StationInfo>();
        StationInfo ee = new StationInfo();
        ee.setId("222");
        ee.setCd("ffdd22");
        stationInfos.add(ee);
        StationInfo ee1 = new StationInfo();
        ee1.setId("2222");
        stationInfos.add(ee1);
        stationService.insertStations(stationInfos);
    }


}
