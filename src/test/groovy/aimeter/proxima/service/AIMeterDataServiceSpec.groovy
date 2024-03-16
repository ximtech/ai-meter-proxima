package aimeter.proxima.service

import aimeter.proxima.IntegrationSpecTemplate
import aimeter.proxima.dto.AIMeterDataSendRequest
import aimeter.proxima.exception.ApiException
import io.iron.ironmq.Queue
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired

import java.time.format.DateTimeParseException

class AIMeterDataServiceSpec extends IntegrationSpecTemplate {

    @Autowired
    AIMeterDataService aiMeterDataService
    @SpringBean(name = 'meterDataQueue')
    Queue meterDataQueue = Mock()

    def 'handleMeterReceivedData() - success meter data send'() {
        given:
        def uuid = createNewMeterDevice(UUID.randomUUID())
        registerMeterDevice(uuid)
        int id = findMeterIdByUUID(uuid)
        createMeterSubscription(id)
        def request = new AIMeterDataSendRequest(uuid, 'photo_2023_12_16_09_04.jpeg', new byte[]{1, 2, 3, 4, 5}, 45, 'image/jpeg')
        
        when:
        aiMeterDataService.handleMeterReceivedData(request)
        
        then: 'dont care about returned message id'
        1 * meterDataQueue.push(_ as String)
        
        and:
        notThrown(ApiException)
        def data = sqlInstance().firstRow("""SELECT * FROM meter.ai_meter_data AS data WHERE data.meter_id = ${id}""")
        data['mime_type'] == request.contentType()
        data['image_name'] == request.imageFileName()
        data['image_size'] == request.imageBytes().length
        data['image_data'] == request.imageBytes()
        data['image_date'] != null
    }

    def 'handleMeterReceivedData() - empty file should fail'() {
        given:
        def uuid = createNewMeterDevice(UUID.randomUUID())
        registerMeterDevice(uuid)
        def request = new AIMeterDataSendRequest(uuid, 'photo_2023_12_16_09_04.jpeg', new byte[]{}, 45, 'image/jpeg')

        when:
        aiMeterDataService.handleMeterReceivedData(request)

        then: 'should not be executed'
        0 * meterDataQueue.push(_ as String)
        
        and:
        def e = thrown(ApiException)
        e.message == 'Empty file'
    }

    def 'handleMeterReceivedData() - invalid file name should fail'() {
        given:
        def uuid = createNewMeterDevice(UUID.randomUUID())
        registerMeterDevice(uuid)
        def request = new AIMeterDataSendRequest(uuid, 'photoz_2023_12_16_09_04.png', new byte[]{1, 2, 3, 4, 5}, 45, 'image/jpeg')

        when:
        aiMeterDataService.handleMeterReceivedData(request)

        then: 'should not be executed'
        0 * meterDataQueue.push(_ as String)

        and:
        def e = thrown(ApiException)
        e.message == 'Invalid file name'
    }

    def 'handleMeterReceivedData() - invalid file date format should fail'() {
        given:
        def uuid = createNewMeterDevice(UUID.randomUUID())
        registerMeterDevice(uuid)
        def request = new AIMeterDataSendRequest(uuid, 'photo_2023_12_16_09-04.jpeg', new byte[]{1, 2, 3, 4, 5}, 45, 'image/jpeg')

        when:
        aiMeterDataService.handleMeterReceivedData(request)

        then: 'should not be executed'
        0 * meterDataQueue.push(_ as String)

        and:
        def e = thrown(DateTimeParseException)
        e.message == "Text '2023_12_16_09-04' could not be parsed at index 13"
    }

    def 'handleMeterReceivedData() - unregistered device should fail'() {
        given:
        def uuid = createNewMeterDevice(UUID.randomUUID())
        def request = new AIMeterDataSendRequest(uuid, 'photo_2023_12_16_09_04.jpeg', new byte[]{1, 2, 3, 4, 5}, 45, 'image/jpeg')

        when:
        aiMeterDataService.handleMeterReceivedData(request)

        then: 'should not be executed'
        0 * meterDataQueue.push(_ as String)

        and:
        def e = thrown(ApiException)
        e.message == "Device with id: [${uuid}] is not registered or not exist"
    }

    def 'handleMeterReceivedData() - device without subscription should fail'() {
        given:
        def uuid = createNewMeterDevice(UUID.randomUUID())
        registerMeterDevice(uuid)
        def request = new AIMeterDataSendRequest(uuid, 'photo_2023_12_16_09_04.jpeg', new byte[]{1, 2, 3, 4, 5}, 45, 'image/jpeg')

        when:
        aiMeterDataService.handleMeterReceivedData(request)

        then: 'should not be executed'
        0 * meterDataQueue.push(_ as String)

        and:
        def e = thrown(ApiException)
        e.message == "Meter with id: [${uuid}] do not have any subscriptions"
    }
}
