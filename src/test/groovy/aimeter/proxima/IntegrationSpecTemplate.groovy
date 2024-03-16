package aimeter.proxima

import aimeter.proxima.dto.AIMeterInitialRegistrationRequest
import aimeter.proxima.dto.GeoIpDTO
import aimeter.proxima.dto.TimeZoneDTO
import aimeter.proxima.service.AIMeterRegistrationService
import aimeter.proxima.service.GeoIPLocationService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class IntegrationSpecTemplate extends DatabaseSpecTemplate {

    @Autowired
    AIMeterRegistrationService aiMeterRegistrationService
    @SpringBean
    GeoIPLocationService geoIPLocationService = Mock() {
        _ * getGeoIpLocationData(_ as String) >> new GeoIpDTO('127.0.0.1',
                new TimeZoneDTO("UTC", 2, 2, "2024-03-14 16:27:25.749+0200", 1.710426445749E9, false, 0))
    }
    
    def createNewMeterDevice(UUID deviceId) {
        sqlInstance().execute("""
        INSERT INTO meter.ai_meter_device (device_id, created_by_user, date_created, date_updated, device_type)
        VALUES ('${deviceId}', 'Roland Popovichj', NOW(), NOW(), 'WATER_METER')""")
        return deviceId
    }
    
    def registerMeterDevice(UUID deviceId) {
        def request = new AIMeterInitialRegistrationRequest(deviceId, 'Test_Device', 95)
        aiMeterRegistrationService.performDeviceInitialRegistration('127.0.0.1', request)
        return deviceId
    }
    
    def createMeterSubscription(long meterId, String type = 'TELEGRAM') {
        sqlInstance().execute("""
        INSERT INTO meter.ai_meter_subscription (meter_id, subscription_type, date_created, date_updated, telegram_chat_id)
        VALUES ('${meterId}', '${type}', NOW(), NOW(), 1234567)""")
    }
    
    def findMeterIdByUUID(UUID deviceId) {
        sqlInstance().firstRow("SELECT device.id FROM meter.ai_meter_device AS device WHERE device_id = '${deviceId}'")['id'] as int
    }
    
}
