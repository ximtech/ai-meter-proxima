package aimeter.proxima.unit

import aimeter.proxima.dto.AIMeterCompleteRegistrationRequest
import jakarta.validation.Validation
import spock.lang.Specification

class CustomValidationsSpec extends Specification {

    def validator = Validation.buildDefaultValidatorFactory().getValidator()

    def 'test AIMeterCompleteRegistrationRequest - custom validation annotations'() {
        expect:
        validator.validate(new AIMeterCompleteRegistrationRequest(deviceId, deviceName, cronExpression, lastExecutionTime, timeZone)).size() == violations

        where:
        deviceId          | deviceName | cronExpression | lastExecutionTime  | timeZone      | violations
        UUID.randomUUID() | 'Test'     | '0 10 8 * * *' | '2024.03.18 23:42' | 'Europe/Riga' | 0
        UUID.randomUUID() | 'Test'     | '0 10 8 * * *' | '2024.03.18 23:42' | 'UTC'         | 0
        UUID.randomUUID() | 'Test'     | '0 10 8 * * *' | '2024.03.18 23:42' | 'GMT'         | 0
        UUID.randomUUID() | 'Test'     | '* * * * * *'  | '2024.03.18 23:42' | 'GMT'         | 0
        UUID.randomUUID() | 'Test'     | '* * * * * *'  | ''                 | 'GMT'         | 1
        UUID.randomUUID() | 'Test'     | '* * * * * *'  | '2024.03.18 23:42' | ''            | 1
        UUID.randomUUID() | 'Test'     | null           | '2024.03.18 23:42' | 'GMT'         | 2    // null and empty
        UUID.randomUUID() | null       | '* * * * * *'  | '2024.03.18 23:42' | 'GMT'         | 2    // null and empty
        UUID.randomUUID() | ''         | '* * * * * *'  | '2024.03.18 23:42' | 'GMT'         | 1
        UUID.randomUUID() | 'Test'     | '* * * * * *'  | '2024.03.1 23:42'  | 'GMT'         | 1    // invalid date
        UUID.randomUUID() | 'Test'     | '* * * * * *'  | '2024-03-18 23:42' | 'GMT'         | 1    // invalid date format
        UUID.randomUUID() | 'Test'     | '* * * * * *'  | '2024.03.18 23:42' | 'ERR'         | 1    // invalid time zone
        UUID.randomUUID() | 'Test'     | '0 10 8 * * '  | '2024.03.18 23:42' | 'GMT'         | 1    // invalid cron

    }

}
