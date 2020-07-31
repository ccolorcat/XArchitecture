package cc.colorcat.login.contract;

import cc.colorcat.login.presenter.LoginPresenter;
import x.common.component.annotation.Stateful;
import x.common.contract.IBase;

/**
 * Author: cxx
 * Date: 2020-07-31
 * GitHub: https://github.com/ccolorcat
 */
public interface ILogin {
    interface View extends IBase.View {
        void setErrorMessage(String msg);

        void jumpToHome();
    }

    @Stateful(LoginPresenter.class)
    interface Presenter extends IBase.Presenter<View> {
        void handleLogin(String username, String password);
    }
}
