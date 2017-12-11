findAll
===
	select 
	@pageTag(){
	* 
	@} 
	from site where 1=1  
	@if(!isEmpty(id)&&id!=""){
		and id like #'%'+id+'%'# 
	@}
	@if(!isEmpty(ct)&&ct!=""){
		and (ct like #'%'+ct+'%'# or cm like #'%'+ct+'%'# or cd like #'%'+ct+'%'# or cu like #'%'+ct+'%'#)  
	@}
	@if(!isEmpty(upkeepStatus)&&upkeepStatus!=""){
		and upkeep_status = #upkeepStatus#
	@}
	@if(!isEmpty(siteStatus)&&siteStatus!=""){
		and site_status = #siteStatus#
	@}
	
insertStations
===
	replace into site(id, cm, ct, cd, cu, upkeep_incom, upkeep_expend, site_incom, site_expend, upkeep_status, site_status) values 
	@trim(){
	@for(site in stationInfos){
	(#site.id#,#site.cm#,#site.ct#,#site.cd#,#site.cu#,#site.upkeepIncom#,#site.upkeepExpend#,#site.siteIncom#,#site.siteExpend#,#site.upkeepStatus#,#site.siteStatus#),
	@}
	@}
	
deleteAllStations
===
	delete from site	
	