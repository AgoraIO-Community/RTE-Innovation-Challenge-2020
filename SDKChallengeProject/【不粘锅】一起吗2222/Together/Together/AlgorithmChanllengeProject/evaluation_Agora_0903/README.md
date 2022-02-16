	
	超分性能评分算法，采用baseline的方案，参赛者提交的模型与baseline从psnr,ssim及cuda的运行时间来综合比较，换算超分模型的综合评分，具体评价方法为：
score = alpha * (psnr_t - psnr_b) + beta * (ssim_t - ssim_b) + gamma * (Time_b - Time_t)/Time_b
其中psnr_t, ssim_t, Time_t分别表示参赛者提交模型在测试集上的psnr、ssim和运行时间，psnr_b, ssim_b, Time_b则对应baseline的指标，alpha, beta和gamma表示权重；
参赛者提交的模型，如果其psnr, ssim, 和time, 劣于baseline，则会出现score为负值，而负值是不能进入复赛的，因此，参赛者提交的模型的各项指标均优于baseline，才有可能得到高分.


	超分评分算法的参数
	--test_model， 模型定义的文件名，程序会根据文件名加载模型的graph, 模型的定义，可参考./model/baseline.py
	--model_path, 模型权重的文件路径
	--baseline_path, baseline模型权重的文件路径
	--LR_path, 测试集low resolution图片的路径
	--HR_path, 测试集high resolution图片的路径
	--upscale, 超分模型上采样的倍数
	--cuda, 是否启用cuda
	--alpha, psnr的权重
	--beta, ssim的权重
	--gamma, 运行时间的权重
	--batch_size, batch size，每次加载图片的数量
	--cycle_num, 测试模型的运行时间的次数，取平均值作为模型的运行时间
	参考者提交模型时，需要注意--test_model，--model_path参数，其它参数无需关注
	
	模型算法复杂度
	评分算法会计算提交模型的FLOPs，对于输入360x240分辨率的图像，提交模型的复杂度要<=2 GFLOPs，否则，取消参赛资格
	
	
	
	
	


