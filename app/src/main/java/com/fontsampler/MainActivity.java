package com.fontsampler;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fontsampler.fragment.FontEditorFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity {

    private static final int PICKFILE_RESULT_CODE = 1;

    private FontEditorFragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            currentFragment = new FontEditorFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, currentFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_add) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/x-font-ttf");
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
            return true;
        }
        if (id == R.id.action_extend) {
            currentFragment.addAdditionalText();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {


                    Uri uri = data.getData();

                    //get name of google drive
                    Cursor cursor = this.getContentResolver().query(uri, new String[]{MediaStore.Files.FileColumns.DISPLAY_NAME}, null, null, null);
                    cursor.moveToFirst();
                    String name = cursor.getString(0);


                    try {
                        File newFile = new File(getExternalCacheDir() + "/" + name);
                        newFile.createNewFile();
                        FileOutputStream out = new FileOutputStream(newFile);
                        InputStream in = getContentResolver().openInputStream(uri);


                        byte[] buf = new byte[1024];
                        int len;

                        try {
                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }

                            in.close();
                            out.flush();
                            out.close();
                        } catch (IOException e) {
                            Log.e(getClass().getSimpleName(),
                                    "Exception transferring file", e);
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    currentFragment.readFontsfromDir();
                }
                break;

        }
    }


}

