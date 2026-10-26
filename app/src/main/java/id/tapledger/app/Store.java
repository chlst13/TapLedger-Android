package id.tapledger.app;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

/** All records stay on-device. Historical NFC observations must never be inferred as transactions. */
public final class Store extends SQLiteOpenHelper {
    public Store(Context c) { super(c,"tapledger.db",null,1); }
    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cards(id TEXT PRIMARY KEY, label TEXT NOT NULL, technology TEXT NOT NULL, last_seen INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE scans(id INTEGER PRIMARY KEY AUTOINCREMENT, card_id TEXT NOT NULL, seen_at INTEGER NOT NULL, technologies TEXT NOT NULL, FOREIGN KEY(card_id) REFERENCES cards(id))");
        db.execSQL("CREATE TABLE transactions(id INTEGER PRIMARY KEY AUTOINCREMENT, card_id TEXT NOT NULL, event_at INTEGER NOT NULL, amount INTEGER NOT NULL, category TEXT NOT NULL, merchant TEXT NOT NULL, location TEXT NOT NULL, note TEXT NOT NULL, origin TEXT NOT NULL, operator TEXT NOT NULL DEFAULT '', UNIQUE(card_id,event_at,amount,category,merchant,location,origin))");
    }
    @Override public void onUpgrade(SQLiteDatabase d,int oldV,int newV) {}
    public void saveScan(String id,String tech) {
        SQLiteDatabase db=getWritableDatabase(); db.beginTransaction();
        try {
            ContentValues c=new ContentValues();c.put("id",id);c.put("label","Kartu ••••"+id.substring(Math.max(0,id.length()-4)));c.put("technology",tech);c.put("last_seen",System.currentTimeMillis());
            db.insertWithOnConflict("cards",null,c,SQLiteDatabase.CONFLICT_IGNORE);
            ContentValues update=new ContentValues();update.put("technology",tech);update.put("last_seen",System.currentTimeMillis());db.update("cards",update,"id=?",new String[]{id});
            ContentValues s=new ContentValues();s.put("card_id",id);s.put("seen_at",System.currentTimeMillis());s.put("technologies",tech);db.insert("scans",null,s);
            db.setTransactionSuccessful();
        } finally {db.endTransaction();}
    }
    public boolean addTransaction(String id,long when,long amount,String category,String merchant,String location,String note,String operator) {
        ContentValues v=new ContentValues();v.put("card_id",id);v.put("event_at",when);v.put("amount",amount);v.put("category",category);v.put("merchant",merchant);v.put("location",location);v.put("note",note);v.put("origin","MANUAL");v.put("operator",operator);
        return getWritableDatabase().insertWithOnConflict("transactions",null,v,SQLiteDatabase.CONFLICT_IGNORE)!=-1;
    }
    public void renameCard(String id,String label) {
        ContentValues v=new ContentValues(); v.put("label",label);
        getWritableDatabase().update("cards",v,"id=?",new String[]{id});
    }
    public ArrayList<String[]> cards() {return rows("SELECT id,label,technology,last_seen FROM cards ORDER BY last_seen DESC",null);}
    public ArrayList<String[]> transactions(String card) {return rows("SELECT CAST(id AS TEXT),card_id,CAST(event_at AS TEXT),CAST(amount AS TEXT),category,merchant,location,note,origin,operator FROM transactions WHERE (?='' OR card_id=?) ORDER BY event_at DESC,id DESC",new String[]{card,card});}
    public int scanCount(){try(Cursor c=getReadableDatabase().rawQuery("SELECT COUNT(*) FROM scans",null)){return c.moveToFirst()?c.getInt(0):0;}}
    private ArrayList<String[]> rows(String sql,String[] args){ArrayList<String[]> out=new ArrayList<>();try(Cursor c=getReadableDatabase().rawQuery(sql,args)){while(c.moveToNext()){String[] a=new String[c.getColumnCount()];for(int i=0;i<a.length;i++)a[i]=c.getString(i);out.add(a);}}return out;}
}
