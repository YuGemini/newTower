$("#fileUpload").fileinput({
    			language: 'zh',
        		uploadUrl: '/station/import.action',
        		uploadAsync:true,
        		allowedPreviewTypes: ['image'],
        		allowedFileExtensions:  ['xls','xlsx'],
        		maxFilePreviewSize: 10240
    		}).on("fileuploaded", function(event, data, previewId, index) {
    			var res = data.response;
    	        if(res.msg=='成功')
    	        {
    	        	document.location = "towerlist";
    	        }
    	    });