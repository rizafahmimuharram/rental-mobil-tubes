package javaaplication12; 



public class Pelanggan {
    private String id;        
    private String nama;      
    private String jenis;    
    private int tahunlahir;   

   
    public Pelanggan(String id, String nama, String jenis, int tahunlahir) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.tahunlahir = tahunlahir;
    }

 
    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getJenis() {
        return jenis;
    }

    public int getTahunLahir() {
        return tahunlahir;
    }
    
   
}

