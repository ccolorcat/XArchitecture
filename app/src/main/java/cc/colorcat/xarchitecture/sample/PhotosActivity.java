package cc.colorcat.xarchitecture.sample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

import cc.colorcat.adapter.AdapterHelper;
import cc.colorcat.adapter.RvAdapter;
import x.common.component.loader.ObjectLoader;
import x.common.view.BaseActivity;

/**
 * Author: cxx
 * Date: 2021-02-07
 * GitHub: https://github.com/ccolorcat
 */
public class PhotosActivity extends BaseActivity {
    private static final String[] URLS = {
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/02/c2/233586351_1601637707436.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/02/c2/233587964_1601637709790.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/02/c2/233593032_1601637712306.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/02/c2/233587965_1601637714714.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/02/c2/233593033_1601637717220.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/02/c2/233587966_1601637719776.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/02/c2/233593034_1601637722326.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/02/c2/233587967_1601637727649.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/02/c2/233593035_1601637730235.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2009/29/c1/233055218_1601348265146.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/16/c1/235514150_1602816016441.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/16/c1/235515925_1602816019657.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/16/c1/235514151_1602816023331.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/16/c1/235515923_1602816000888.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/16/c1/235514149_1602816007035.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/16/c1/235515924_1602816012017.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2009/29/c1/233055221_1601348280545.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2009/29/c1/233055197_1601348272830.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/29/c0/237695384_1603931696026.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2010/02/c2/233586351_1601637707436.jpg",
            "https://img.pconline.com.cn/images/upload/upc/tx/photoblog/2003/09/c4/196217163_1583737997394.jpg",
            "https://up.enterdesk.com/edpic_source/b4/71/23/b47123e5f5ae76ae39b2f009e840226c.jpg"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        RecyclerView rv = findViewById(R.id.rv_photos);
        rv.setLayoutManager(new LinearLayoutManager(this));
        RvAdapter adapter = AdapterHelper.newFixedRvAdapter(Arrays.asList(URLS), R.layout.item_photo, (holder, data) -> {
            ObjectLoader.with(holder.getRoot())
                    .load(data)
                    .asImage()
                    .into(holder.get(R.id.iv_photo));
        });
        rv.setAdapter(adapter);
    }
}
