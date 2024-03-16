package aimeter.proxima.service

import aimeter.proxima.IntegrationSpecTemplate
import aimeter.proxima.dto.AIMeterCompleteRegistrationRequest
import aimeter.proxima.dto.AIMeterInitialRegistrationRequest
import aimeter.proxima.dto.GeoIpDTO
import aimeter.proxima.dto.TimeZoneDTO
import aimeter.proxima.exception.ApiException
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired

class AIMeterRegistrationServiceSpec extends IntegrationSpecTemplate {
    
    @Autowired
    AIMeterRegistrationService aiMeterRegistrationService
    @SpringBean
    GeoIPLocationService geoIPLocationService = Mock()
    
    def 'performDeviceInitialRegistration() - success registration'() {
        given:
        def uuid = createNewMeterDevice(UUID.randomUUID())
        def request = new AIMeterInitialRegistrationRequest(uuid, 'Test_Device', 95)
        String ipAddress = '127.0.0.1'
        
        when: 'geo ip dto mocked, no need to check response'
        aiMeterRegistrationService.performDeviceInitialRegistration(ipAddress, request)
        
        then:
        1 * geoIPLocationService.getGeoIpLocationData(ipAddress) >> new GeoIpDTO(ipAddress, 
                new TimeZoneDTO("UTC", 2, 2, "2024-03-14 16:27:25.749+0200", 1.710426445749E9, false, 0))
        
        and: 'check that device updated'
        def device = sqlInstance().firstRow("SELECT * FROM meter.ai_meter_device WHERE device_id = '${uuid}'")
        device != null
        device['registered'] == true
        device['deleted'] == false
        device['battery_level'] == request.batteryLevel()
        device['config_id'] != null
        
        and: 'check that meter config created'
        def config = sqlInstance().firstRow("""
                SELECT * FROM meter.ai_meter_config AS config
                JOIN meter.ai_meter_device AS device ON config.id = device.config_id
                WHERE device.device_id = '${uuid}'""")
        config != null
        config['device_name'] == request.deviceName()
        config['device_ip'] == ipAddress
        config['device_time_zone'] == 'UTC'
        config['cron_expression'] == null
        config['last_execution_time'] == null
    }

    def 'performDeviceInitialRegistration() - not existing device should throw error'() {
        given: 'not existing uuid'
        def uuid = UUID.randomUUID()
        def request = new AIMeterInitialRegistrationRequest(uuid, 'Test_Device', 95)
        String ipAddress = '127.0.0.1'

        when:
        aiMeterRegistrationService.performDeviceInitialRegistration(ipAddress, request)

        then: 'should not be called'
        0 * geoIPLocationService.getGeoIpLocationData(ipAddress)

        and:
        def e = thrown(ApiException)
        e.message == "Device with id: [${uuid}] do not exist"
    }

    def 'performDeviceCompleteRegistration() - success full registration'() {
        given:
        def uuid = createNewMeterDevice(UUID.randomUUID())
        def request = new AIMeterInitialRegistrationRequest(uuid, 'Test_Device', 95)
        String ipAddress = '127.0.0.1'

        when: 'register existing device'
        aiMeterRegistrationService.performDeviceInitialRegistration(ipAddress, request)

        then:
        1 * geoIPLocationService.getGeoIpLocationData(ipAddress) >> new GeoIpDTO(ipAddress,
                new TimeZoneDTO("UTC", 2, 2, "2024-03-14 16:27:25.749+0200", 1.710426445749E9, false, 0))
        
        and: 'complete registration'
        def completeRequest = new AIMeterCompleteRegistrationRequest(uuid, 'Name changed', '0 10 8 * * *', '2024.01.25 16:41', 'Europe/Riga')
        aiMeterRegistrationService.performDeviceCompleteRegistration(completeRequest)
        
        and: 'check that meter config updated'
        def config = sqlInstance().firstRow("""
                SELECT * FROM meter.ai_meter_config AS config
                JOIN meter.ai_meter_device AS device ON config.id = device.config_id
                WHERE device.device_id = '${uuid}'""")
        config != null
        config['device_name'] == completeRequest.deviceName()
        config['device_ip'] == ipAddress
        config['device_time_zone'] == completeRequest.timeZone()
        config['cron_expression'] == completeRequest.cronExpression()
        config['last_execution_time'] != null
    }

    def 'performDeviceCompleteRegistration() - not registered device should fail'() {
        given: 'existing, but not registered device'
        def uuid = createNewMeterDevice(UUID.randomUUID())
        def request = new AIMeterCompleteRegistrationRequest(uuid, 'Test_Device', '0 10 8 * * *', '2024.01.25 16:41', 'Europe/Riga')

        when:
        aiMeterRegistrationService.performDeviceCompleteRegistration(request)

        then:
        def e = thrown(ApiException)
        e.message == "Device with id: [${uuid}] is not registered or not exist"
    }
}
