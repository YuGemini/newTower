$("#fileUpload").fileinput({
    			language: 'zh',
        		uploadUrl: '/station/import.action',
        		uploadAsync:true,
        		allowedPreviewTypes: ['image'],
        		allowedFileExtensions:  ['xls','xlsx'],
        		maxFilePreviewSize: 10240
    		}).on("fileuploaded", function(event, data) {
    			alert(data);
    			var data = $.parseJSON(data); 
    	        if(data=='success')
    	        {
    	            alert('处理成功');
    	        }
    	    });