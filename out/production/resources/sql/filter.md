getFilterId
===
	SELECT FILTER_ID.NEXTVAL from DUAL
	
findFilterByUser
===
	select * from vst_filter where user_id=#userid# 	
	
updateFilter
=== 
	update vst_filter set STATUS = '0' where user_id=#userid# and status='1' 		