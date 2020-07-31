package cc.colorcat.login.presenter;

import androidx.annotation.NonNull;

import java.util.List;

import cc.colorcat.login.contract.ILogin;
import cc.colorcat.login.entity.Course;
import cc.colorcat.login.model.LoginModel;
import x.common.component.Hummingbird;
import x.common.component.Lazy;
import x.common.component.log.Logger;
import x.common.component.network.ApiCallback;
import x.common.component.network.ApiException;
import x.common.entity.Result;

/**
 * Author: cxx
 * Date: 2020-07-31
 * GitHub: https://github.com/ccolorcat
 */
public class LoginPresenter implements ILogin.Presenter {
    private final Logger mLogger = Logger.getLogger(this);
    private final Lazy<LoginModel> mModel = Lazy.by(() -> Hummingbird.visit(LoginModel.class));

    @Override
    public void handleLogin(String username, String password) {
        mModel.get().listCourses(4, 30)
                .enqueue(requireView(), new ApiCallback<Result<List<Course>>>() {
                    @Override
                    public void onStart() {
                        mLogger.v("onStart");
                        requireView().showLoading();
                    }

                    @Override
                    public void onSuccess(@NonNull Result<List<Course>> data) {
                        mLogger.d("onSuccess " + data);
                        requireView().jumpToHome();
                    }

                    @Override
                    public void onFailure(@NonNull ApiException cause) {
                        mLogger.w("onFailure " + cause);
                        requireView().setErrorMessage(cause.getMessage());
                    }

                    @Override
                    public void onFinish() {
                        mLogger.v("onFinish");
                        requireView().hideLoading();
                    }
                });
    }
}
