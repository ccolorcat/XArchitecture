package cc.colorcat.login.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cc.colorcat.login.R;
import cc.colorcat.login.contract.ILogin;
import x.common.component.Lazy;
import x.common.component.loader.ObjectLoader;
import x.common.component.log.Logger;
import x.common.view.BaseFragment;
import x.common.view.XHolder;

/**
 * Author: cxx
 * Date: 2020-07-31
 * GitHub: https://github.com/ccolorcat
 */
public class LoginFragment extends BaseFragment implements ILogin.View {
    private Lazy<ILogin.Presenter> mPresenter = Lazy.by(() -> bindAndGet(ILogin.Presenter.class));

    @Override
    protected int getLayoutId() {
        super.getLayoutId();
        return R.layout.login_fragment_login;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final XHolder holder = XHolder.from(view);
        holder.setClick(R.id.confirm, (v) -> {
            mPresenter.get().handleLogin(holder.getString(R.id.username), holder.getString(R.id.password));
        });
        ObjectLoader.with(this)
                .load("https://i0.hdslb.com/bfs/album/8d5baf53a12b8c2ac81dcb8e28385b79cb07459a.jpg")
                .asImage()
                .circleCrop()
                .into(holder.get(R.id.iv_avatar));
    }

    @Override
    public void setErrorMessage(String msg) {
        Logger.getLogger(this).w("login failed: " + msg);
    }

    @Override
    public void jumpToHome() {
        Logger.getLogger(this).v("jump to home");
    }
}
