"""
@project: mobile_sr_evaluation
@author: sfzhou
@file: dataloader.py
@ide: PyCharm
@time: 2019/5/14 16:26

"""
from torchvision.transforms import ToTensor
import os
from torch.utils.data import Dataset
import PIL.Image as pil_image

class TestDataset(Dataset):

    def __init__(self, HR_path, LR_path):
        super(TestDataset, self).__init__()

        self.HR_root = HR_path
        self.LR_root = LR_path
        self.HR_paths = sorted(os.listdir(HR_path))
        self.LR_paths = sorted(os.listdir(LR_path))
        assert len(self.HR_paths) == len(self.LR_paths)

    def __getitem__(self, index):

        HR_path = os.path.join(self.HR_root, self.HR_paths[index])
        HR_image = pil_image.open(HR_path).convert('RGB')

        LR_path = os.path.join(self.LR_root, self.LR_paths[index])
        LR_image = pil_image.open(LR_path).convert('RGB')

        LR_tensor = ToTensor()(LR_image)
        HR_tensor = ToTensor()(HR_image)
        return LR_tensor, HR_tensor

    def __len__(self):
        return len(self.HR_paths)
