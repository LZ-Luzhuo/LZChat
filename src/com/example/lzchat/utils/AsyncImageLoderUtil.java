package com.example.lzchat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

import com.example.lzchat.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

/**
 * =================================================
 * <p/>
 * 作者:卢卓
 * <p/>
 * 版本:1.0
 * <p/>
 * 创建日期:2015/11/10 19:59
 * <p/>
 * 描述:异步加载图片工具
 * <p/>
 * 修订历史:
 * <p/>
 * <p/>
 * =================================================
 **/
public class AsyncImageLoderUtil {
    /**
     * 配置全局的 Android-Universal-Image-Loader
     */
    private static AsyncImageLoderUtil instance = null;

    private ImageLoader mImageLoader;

    // 列表中默认的图片
    private DisplayImageOptions mListItemOptions;

    public AsyncImageLoderUtil(Context context){
        mImageLoader = ImageLoader.getInstance();
        mListItemOptions = new DisplayImageOptions.Builder()
            // 设置图片Uri为空或是错误的时候显示的图片
            .showImageForEmptyUri(R.drawable.message_image_bg)
            // 设置底图
            .showStubImage(R.drawable.message_image_bg)
            // 设置图片加载/解码过程中错误时候显示的图片
            .showImageOnFail(R.drawable.message_image_bg)
            // 加载图片时会在内存、磁盘中加载缓存
            .cacheInMemory()
            .cacheOnDisc()
            .bitmapConfig(Bitmap.Config.RGB_565)
            .delayBeforeLoading(300)
            .build();
    }

    public static AsyncImageLoderUtil getInstance() {
        return instance;
    }

    public synchronized static AsyncImageLoderUtil init(Context context) {
        if (instance == null) {
            instance = new AsyncImageLoderUtil(context);
        }

        File cacheDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(
                Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                // .imageDownloader(imageDownloader).imageDecoder(imageDecoder)
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).memoryCacheExtraOptions(
                        360, 360).memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024)).discCache(
                        new UnlimitedDiskCache(cacheDir)).build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        return instance;
    }

    /**
     * 显示列表图片
     * @param uri
     * @param imageView
     */
    public void displayListItemImage(String uri, ImageView imageView) {
        String strUri = (isEmpty(uri) ? "" : uri);
        mImageLoader.displayImage(strUri, imageView, mListItemOptions);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    private boolean isEmpty(String str) {
        if (str != null && str.trim().length() > 0 && !str.equalsIgnoreCase(null)) {
            return false;
        }
        return true;
    }
}
