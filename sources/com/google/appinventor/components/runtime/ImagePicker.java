package com.google.appinventor.components.runtime;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.webkit.MimeTypeMap;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.MediaUtil;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

@SimpleObject
@DesignerComponent(category = ComponentCategory.MEDIA, description = "A special-purpose button. When the user taps an image picker, the device's image gallery appears, and the user can choose an image. After an image is picked, it is saved, and the <code>Selected</code> property will be the name of the file where the image is stored. In order to not fill up storage, a maximum of 10 images will be stored.  Picking more images will delete previous images, in order from oldest to newest.", docUri = "image/image-picker", version = 5)
@UsesPermissions(permissionNames = "android.permission.WRITE_EXTERNAL_STORAGE")
public class ImagePicker extends Picker implements ActivityResultListener {
    private static final String FILE_PREFIX = "picked_image";
    private static final String LOG_TAG = "ImagePicker";
    private static final String imagePickerDirectoryName = "/Pictures/_app_inventor_image_picker";
    private static int maxSavedFiles = 10;
    private boolean havePermission = false;
    private String selectionSavedImage = "";
    private String selectionURI;

    /* renamed from: com.google.appinventor.components.runtime.ImagePicker$2 */
    class C03392 implements Comparator<File> {
        C03392() {
        }

        public int compare(File f1, File f2) {
            return Long.valueOf(f1.lastModified()).compareTo(Long.valueOf(f2.lastModified()));
        }
    }

    /* renamed from: com.google.appinventor.components.runtime.ImagePicker$1 */
    class C06171 implements PermissionResultHandler {
        C06171() {
        }

        public void HandlePermissionResponse(String permission, boolean granted) {
            if (granted) {
                ImagePicker.this.havePermission = true;
                ImagePicker.this.click();
                return;
            }
            ImagePicker.this.container.$form().dispatchPermissionDeniedEvent(ImagePicker.this, "Click", permission);
        }
    }

    public ImagePicker(ComponentContainer container) {
        super(container);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Path to the file containing the image that was selected.")
    public String Selection() {
        return this.selectionSavedImage;
    }

    protected Intent getIntent() {
        return new Intent("android.intent.action.PICK", Media.INTERNAL_CONTENT_URI);
    }

    public void click() {
        if (this.havePermission) {
            super.click();
        } else {
            this.container.$form().askPermission("android.permission.WRITE_EXTERNAL_STORAGE", new C06171());
        }
    }

    public void resultReturned(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode && resultCode == -1) {
            Uri selectedImage = data.getData();
            this.selectionURI = selectedImage.toString();
            Log.i(LOG_TAG, "selectionURI = " + this.selectionURI);
            ContentResolver cR = this.container.$context().getContentResolver();
            String extension = "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(cR.getType(selectedImage));
            Log.i(LOG_TAG, "extension = " + extension);
            saveSelectedImageToExternalStorage(extension);
            AfterPicking();
        }
    }

    private void saveSelectedImageToExternalStorage(String extension) {
        if (this.container.$form().isDeniedPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
            this.container.$form().dispatchPermissionDeniedEvent((Component) this, LOG_TAG, "android.permission.WRITE_EXTERNAL_STORAGE");
            return;
        }
        this.selectionSavedImage = "";
        try {
            File tempFile = MediaUtil.copyMediaToTempFile(this.container.$form(), this.selectionURI);
            Log.i(LOG_TAG, "temp file path is: " + tempFile.getPath());
            copyToExternalStorageAndDeleteSource(tempFile, extension);
        } catch (IOException e) {
            Log.i(LOG_TAG, "copyMediaToTempFile failed: " + e.getMessage());
            this.container.$form().dispatchErrorOccurredEvent(this, LOG_TAG, ErrorMessages.ERROR_CANNOT_COPY_MEDIA, e.getMessage());
        }
    }

    private void copyToExternalStorageAndDeleteSource(File source, String extension) {
        File dest = null;
        File destDirectory = new File(Environment.getExternalStorageDirectory() + imagePickerDirectoryName);
        try {
            destDirectory.mkdirs();
            dest = File.createTempFile(FILE_PREFIX, extension, destDirectory);
            this.selectionSavedImage = dest.getPath();
            Log.i(LOG_TAG, "saved file path is: " + this.selectionSavedImage);
            FileUtil.copyFile(source.getAbsolutePath(), dest.getAbsolutePath());
            Log.i(LOG_TAG, "Image was copied to " + this.selectionSavedImage);
        } catch (IOException e) {
            Log.i(LOG_TAG, "copyFile failed. " + ("destination is " + this.selectionSavedImage + ": error is " + e.getMessage()));
            this.container.$form().dispatchErrorOccurredEvent(this, "SaveImage", ErrorMessages.ERROR_CANNOT_SAVE_IMAGE, err);
            this.selectionSavedImage = "";
            dest.delete();
        }
        source.delete();
        trimDirectory(maxSavedFiles, destDirectory);
    }

    private void trimDirectory(int maxSavedFiles, File directory) {
        File[] files = directory.listFiles();
        Arrays.sort(files, new C03392());
        int excess = files.length - maxSavedFiles;
        for (int i = 0; i < excess; i++) {
            files[i].delete();
        }
    }
}
