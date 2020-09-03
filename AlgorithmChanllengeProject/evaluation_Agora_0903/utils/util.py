"""
@project: mobile_sr_evaluation
@author: sfzhou
@file: utils.py
@ide: PyCharm
@time: 2019/5/14 16:55

"""
import numpy as np
import math
from skimage.measure import compare_ssim
import torch
from collections import OrderedDict
import cv2
import os

def psnr(img1, img2):
    assert img1.dtype == img2.dtype == np.uint8, 'np.uint8 is supposed.'
    img1 = img1.astype(np.float64)
    img2 = img2.astype(np.float64)
    mse = np.mean((img1 - img2)**2)
    if mse == 0:
        return float('inf')
    return 20 * math.log10(255.0 / math.sqrt(mse))


def ssim(img1, img2, multichannel=True):
    assert img1.dtype == img2.dtype == np.uint8, 'np.uint8 is supposed.'
    return compare_ssim(img1, img2, multichannel=multichannel)

def load_state_dict(path):

    state_dict = torch.load(path, map_location='cpu')
    new_state_dcit = OrderedDict()
    for k, v in state_dict.items():
        if 'module' in k:
            name = k[7:]
        else:
            name = k
        new_state_dcit[name] = v
    return new_state_dcit

def sr_forward_psnr(dataloader, model, device, crop_boarder, save_path=None):

    psnrs = []
    ssims = []
    for b, (lr_imgs, hr_imgs) in enumerate(dataloader):

        lr_imgs = lr_imgs.to(device)
        sr_imgs = model(lr_imgs)

        for i in range(sr_imgs.size(0)):
            sr_img = sr_imgs[i,:,:,:]
            hr_img = hr_imgs[i,:,:,:]
            sr_img = sr_img.cpu().mul(255).clamp(0,255).byte().squeeze().permute(1,2,0).numpy()
            hr_img = hr_img.cpu().mul(255).clamp(0,255).byte().squeeze().permute(1,2,0).numpy()

            if save_path:
                sr_img = cv2.cvtColor(sr_img, cv2.COLOR_RGB2BGR)
                hr_img = cv2.cvtColor(hr_img, cv2.COLOR_RGB2BGR)

                cv2.imwrite(os.path.join(save_path,'SR', '{:04d}.jpg'.format(b+1)), sr_img)
                cv2.imwrite(os.path.join(save_path,'GT', '{:04d}.jpg'.format(b+1)), hr_img)


            sr_img = sr_img[crop_boarder:-crop_boarder, crop_boarder:-crop_boarder, :]
            hr_img = hr_img[crop_boarder:-crop_boarder, crop_boarder:-crop_boarder, :]

            psnrs.append(psnr(sr_img, hr_img))
            ssims.append(ssim(sr_img, hr_img))

    avg_psnr = np.mean(psnrs)
    avg_ssim = np.mean(ssims)

    return avg_psnr, avg_ssim


def sr_forward_time(dataloader, model, device):

    cuda_time = 0.0
    for lr_imgs, hr_imgs in dataloader:

        lr_imgs = lr_imgs.to(device)
        start_event = torch.cuda.Event(enable_timing=True)
        end_event = torch.cuda.Event(enable_timing=True)
        start_event.record()
        sr_imgs = model(lr_imgs)

        end_event.record()
        torch.cuda.synchronize(device)

        cuda_time += start_event.elapsed_time(end_event)

    return cuda_time
