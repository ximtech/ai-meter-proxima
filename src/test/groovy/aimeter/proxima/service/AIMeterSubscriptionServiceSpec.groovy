package aimeter.proxima.service

import aimeter.proxima.IntegrationSpecTemplate
import aimeter.proxima.exception.ApiException
import aimeter.proxima.service.AIMeterSubscriptionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class AIMeterSubscriptionServiceSpec extends IntegrationSpecTemplate {

    @Autowired
    AIMeterSubscriptionService aiMeterSubscriptionService

    def 'getTelegramSubscriptionOTP() - success pin code creation'() {
        given:
        def uuid = createNewMeterDevice(UUID.randomUUID())
        registerMeterDevice(uuid)

        when:
        def result = aiMeterSubscriptionService.getTelegramSubscriptionOTP(uuid)

        then:
        result.botName() == 'AiMeter_bot'
        result.pinCode().length() == 4
        result.pinCode().toInteger() <= 9999

        and:
        def token = sqlInstance().firstRow("""
                SELECT * FROM meter.ai_meter_token AS token 
                WHERE token.access_token = ${result.pinCode()}""")
        token['token_type'] == 'TELEGRAM_OTP'
        token['access_token'] == result.pinCode()
    }

    def 'getTelegramSubscriptionOTP() - not registered device should fail'() {
        given:
        def uuid = createNewMeterDevice(UUID.randomUUID())
        
        when:
        aiMeterSubscriptionService.getTelegramSubscriptionOTP(uuid)
        
        then:
        def e = thrown(ApiException)
        e.message == "Device with id: [${uuid}] is not registered or not exist"
    }
}
