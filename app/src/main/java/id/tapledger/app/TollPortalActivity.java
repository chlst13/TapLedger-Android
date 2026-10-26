package id.tapledger.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Official-site viewer: does not scrape, intercept, or transmit a card number itself. */
public class TollPortalActivity extends Activity {
    private static final String ASTRA = "https://astratol.co.id/";
    private WebView web;
    private String portalUrl=ASTRA;
    private int dp(int a) {return (int)(a*getResources().getDisplayMetrics().density+.5f);}
    private Button button(String title, LinearLayout parent, View.OnClickListener click) {
        Button b=new Button(this); b.setText(title); b.setTextAllCaps(false); parent.addView(b); b.setOnClickListener(click); return b;
    }
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout layout=new LinearLayout(this);layout.setOrientation(LinearLayout.VERTICAL);layout.setPadding(dp(9),dp(8),dp(9),dp(5));setContentView(layout);
        TextView title=new TextView(this);title.setText("Struk tol dari situs operator");title.setTextSize(21);layout.addView(title);
        TextView notice=new TextView(this);notice.setText("Sumber: AstraTol (situs resmi). Masukkan sendiri nomor kartu dan periode pada situs. Halaman ini bukan sistem TapLedger. Jangan masukkan PIN/OTP.");notice.setTextSize(12);layout.addView(notice);
        LinearLayout actions=new LinearLayout(this); actions.setOrientation(LinearLayout.HORIZONTAL); layout.addView(actions);
        button("Salin no. kartu",actions,v->copyCard());
        button("Simpan PDF",actions,v->printPage());
        button("Browser",actions,v->startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(portalUrl))));
        web=new WebView(this);
        web.getSettings().setJavaScriptEnabled(true); // Public website may require JS.
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setAllowFileAccess(false);
        web.getSettings().setAllowContentAccess(false);
        web.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view,android.webkit.WebResourceRequest req) {
                Uri uri=req.getUrl();
                if("https".equalsIgnoreCase(uri.getScheme()) && "astratol.co.id".equalsIgnoreCase(uri.getHost()))return false;
                new AlertDialog.Builder(TollPortalActivity.this).setTitle("Buka situs lain?")
                    .setMessage("Alamat tujuan: "+uri.toString()+"\n\nSitus eksternal akan dibuka di browser, bukan dalam TapLedger.")
                    .setNegativeButton("Batal",null)
                    .setPositiveButton("Buka",(d,w)->startActivity(new Intent(Intent.ACTION_VIEW,uri))).show();
                return true;
            }
        });
        layout.addView(web,new LinearLayout.LayoutParams(-1,0,1));
        web.loadUrl(portalUrl);
    }
    private void copyCard(){
        // NFC tag UID is NOT necessarily the printed issuer card number.
        new AlertDialog.Builder(this).setTitle("Nomor kartu")
            .setMessage("Nomor seri NFC yang terdeteksi belum tentu sama dengan nomor kartu untuk pencarian struk. Gunakan nomor kartu yang tercetak pada kartu atau nomor terverifikasi dari penerbit. Salin nomor kartu dari aplikasi penerbit lalu tempelkan pada situs.")
            .setPositiveButton("Mengerti",null).show();
    }
    private void printPage(){
        if(web==null || web.getProgress()<100){new AlertDialog.Builder(this).setMessage("Tunggu sampai halaman selesai dimuat.").setPositiveButton("OK",null).show();return;}
        PrintManager pm=(PrintManager)getSystemService(Context.PRINT_SERVICE);
        pm.print("Struk AstraTol - situs operator",web.createPrintDocumentAdapter("Struk Operator"),new PrintAttributes.Builder().build());
    }
    @Override public void onBackPressed(){if(web!=null&&web.canGoBack())web.goBack();else super.onBackPressed();}
    @Override protected void onDestroy(){if(web!=null){web.destroy();web=null;}super.onDestroy();}
}
