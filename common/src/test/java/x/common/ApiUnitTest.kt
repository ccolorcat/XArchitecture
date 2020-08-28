package x.common

import org.junit.Test
import x.common.component.Hummingbird
import x.common.component.log.Logger
import x.common.test.TestManager

/**
 * Author: cxx
 * Date: 2020-08-28
 * GitHub: https://github.com/ccolorcat
 */
class ApiUnitTest {
    init {
        TestManager.init()
    }

    @Test
    fun testLogin() {
        val service = Hummingbird.visit(AccountService::class.java)
        val result = service.loginWithPassword("18986430015", "cl031018").execute()
        Logger.getDefault().v("testLogin: $result")
    }
}