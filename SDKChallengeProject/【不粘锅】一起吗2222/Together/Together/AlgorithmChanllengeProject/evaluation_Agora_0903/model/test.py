import torch
from torch import nn
class model(nn.Module):
    def __init__(self, scale_factor=2, n_feats=8, expansion_ratio=2):
        super(model, self).__init__()
        head = [nn.Conv2d(3, n_feats, kernel_size=3, padding=1)]
        self.body1 = nn.Sequential(
            nn.Conv2d(n_feats, n_feats * expansion_ratio, kernel_size=3, padding=1),
            nn.PReLU(),
            nn.Conv2d(n_feats * expansion_ratio, n_feats, kernel_size=3, padding=1)
        )
        self.body2 = nn.Sequential(
            nn.Conv2d(n_feats, n_feats * expansion_ratio, kernel_size=3, padding=1),
            nn.PReLU(),
            nn.Conv2d(n_feats * expansion_ratio, n_feats, kernel_size=3, padding=1)
        )
        self.body3 = nn.Sequential(
            nn.Conv2d(n_feats, n_feats * expansion_ratio, kernel_size=3, padding=1),
            nn.PReLU(),
            nn.Conv2d(n_feats * expansion_ratio, n_feats, kernel_size=3, padding=1)
        )
        self.body4 = nn.Sequential(
            nn.Conv2d(n_feats, n_feats * expansion_ratio, kernel_size=3, padding=1),
            nn.PReLU(),
            nn.Conv2d(n_feats * expansion_ratio, n_feats, kernel_size=3, padding=1)
        )
        self.body5 = nn.Sequential(
            nn.Conv2d(n_feats, n_feats * expansion_ratio, kernel_size=3, padding=1),
            nn.PReLU(),
            nn.Conv2d(n_feats * expansion_ratio, n_feats, kernel_size=3, padding=1)
        )
        self.body6 = nn.Sequential(
            nn.Conv2d(n_feats, n_feats * expansion_ratio, kernel_size=3, padding=1),
            nn.PReLU(),
            nn.Conv2d(n_feats * expansion_ratio, n_feats, kernel_size=3, padding=1)
        )
        self.body7 = nn.Sequential(
            nn.Conv2d(n_feats, n_feats * expansion_ratio, kernel_size=3, padding=1),
            nn.PReLU(),
            nn.Conv2d(n_feats * expansion_ratio, n_feats, kernel_size=3, padding=1)
        )
        self.body8 = nn.Sequential(
            nn.Conv2d(n_feats, n_feats * expansion_ratio, kernel_size=3, padding=1),
            nn.PReLU(),
            nn.Conv2d(n_feats * expansion_ratio, n_feats, kernel_size=3, padding=1)
        )
        self.conv = nn.Conv2d(n_feats, n_feats, kernel_size=3, padding=1)

        tail = [nn.Conv2d(n_feats, 3 * (scale_factor ** 2), kernel_size=3, padding=1),
                nn.PixelShuffle(scale_factor)]

        self.head = nn.Sequential(*head)
        self.tail = nn.Sequential(*tail)

    def forward(self, x):
        x = self.head(x)
        skip = x
        x = x + self.body1(x)
        x = x + self.body2(x)
        x = x + self.body3(x)
        x = x + self.body4(x)
        x = x + self.body5(x)
        x = x + self.body6(x)
        x = x + self.body7(x)
        x = x + self.body8(x)
        x = self.conv(x)
        x += skip
        x = self.tail(x)

        return x

