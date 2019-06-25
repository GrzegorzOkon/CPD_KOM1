#################################################################
# Autor Grzegorz Okoñ - G³ówny programista
#
# Sprawdzanie ostatnich komunikatów odes³anych z systemów AES/AIS
#################################################################
open (ETYKIETA_ILOSCI, ">CPD_E_KOM1.txt") || die "nie moge utworzyc pliku";

@baty=('zapytanie_228_aesdb.bat','zapytanie_229_aisdb.bat');

foreach $bat (@baty) {
   $warunek_pierwszy = 0;
   $warunek_drugi = 0;
   $ilosc = -1;
   $komunikat = -1;

   open (plik,"$bat|");
   open (ETYKIETA_ILOSCI, ">>CPD_E_KOM1.txt") || die "nie moge zapisac do pliku";
   open (ETYKIETA_CZASU, ">>CPD_E_KOM1.txt") || die "nie moge zapisac do pliku";
   
   if ($bat=~/aesdb/) {
      print ETYKIETA_ILOSCI "* Ostatnie wys³ane komunikaty *\n\n";
      print ETYKIETA_ILOSCI "* System AES\n\n";
   } elsif ($bat=~/aisdb/) {
      print ETYKIETA_ILOSCI "\n\n* System AIS\n\n";
   }

   while (<plik>) {   
      if(/Komunikatow/) {
         $warunek_pierwszy = 1;
         print ETYKIETA_ILOSCI "iloœæ    komunikat\n-----    ---------\n";
      }
      if(/doc_time/) {
         $warunek_pierwszy = 0;
         $warunek_drugi = 1;
         print ETYKIETA_CZASU "\n\ndoc_time               xml_name    ref_no\n-------------------    --------    ------------------\n";       
      }    
      if($warunek_pierwszy == 1) {
         if(/\s+(\d+)\s+/) { 
            $ilosc = $1;   
         }  
         if(/\s+(\IE\d+)\s+/) { 
            $komunikat = $1; 
            write (ETYKIETA_ILOSCI); 
         } 
   
      }
      if($warunek_drugi == 1) { 
         if(/\s+(\w+\s+\d+\s+\d+\s+\d+\:\d+\w+)/) { 
            $doc_time = $1;  
         } 
         if(/\s+(\IE\d+)\s+/) {  
            $xml_name = $1; 
         } 
         if(/\s+(\d+\w+\d+\w+\d+)\s+/) {  
            $ref_no = $1; 
            write (ETYKIETA_CZASU); 
         } 
      }
   }
   close plik;
}

format ETYKIETA_ILOSCI =
@####    @<<<<<<<<<<    
$ilosc,  $komunikat
.

format ETYKIETA_CZASU =
@<<<<<<<<<<<<<<<<<<    @<<<<<<     @<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
$doc_time,             $xml_name,  $ref_no
.