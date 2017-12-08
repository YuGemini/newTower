findAllReport
===
	select a.*,b.full_name user_name from vst_doc a join vst_user b on a.user_id = b.id

findLabelByUserId
===
    SELECT LABEL FROM vst_label where USER_ID = #userid# GROUP BY LABEL ORDER BY count(LABEL) DESC
    
findDocById
===
	select * from vst_doc where id = #id#  

findDocByIds
===
	select * from vst_doc where id in ( #join(ids)#)  
		
findDocByDept
===
	select a.*,b.full_name user_name from vst_doc a join vst_user b on a.user_id = b.id where b.pd_id = (select pd_id from vst_user where id = #userid#)	
	
findDocByUser
===
	select a.*,b.full_name user_name from vst_doc a join vst_user b on a.user_id = b.id where a.user_id=#userid#
	
getReportId
===
	SELECT DOC_ID.NEXTVAL from DUAL
	
insertDoc
===	
	insert into vst_doc (id,title,doc_state,label,pic_name,pic_url,user_id,point_info,update_time) values (#id#,#title#,#docState#,#label#,#picName#,#picUrl#,#userId#,#pointInfo#,#updateTime#)
	
	