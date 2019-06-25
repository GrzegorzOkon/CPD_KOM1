use aesdb 
go 
select 'Ilosc Komunikatow na bazie'
select top 10 count(xml_name) as Ilosc, xml_name as Komunikat from aesdb..customs_message where doc_time >= dateadd(hh,-1,getdate()) and xml_name like 'IE%' group by xml_name order by Ilosc desc at isolation read uncommitted  
go
select 'Ostatnie wyslane komunikaty'
select top 20 doc_time, xml_name, ref_no from aesdb..customs_message where doc_time >= dateadd(hh,-1,getdate()) and xml_name in('IE528','IE529') order by doc_time desc at isolation read uncommitted  
go