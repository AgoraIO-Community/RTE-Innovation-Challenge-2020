# RTC Algorithm Challenge 2020

## Introduction

Ultra-resolution is one of the core algorithms in the field of computer vision, attracting the attention of academia and industry, with the aim of restoring high-resolution, clear images (videos) from low-resolution images (videos). This technology has important practical value in real-time communication (RTC), and how to apply the super-resolution algorithm to RTC is an urgent problem to be solved by industry. The aim of this competition is to attract more researchers to participate in the study of super-resolution algorithms, promote the application of super-resolution algorithms in RTC scenes, and promote in-depth collaboration between industry and academia.

## Task

When using the super-resolution algorithm to process real-time video streaming, the processing performance and computing performance of the model are a dilemma. In order to pursue lower complexity, it may be necessary to sacrifice image quality, in order to pursue higher quality output, resulting in the equipment resource is over-occupied, resulting in equipment hot, video fuzzy Cardon and other phenomena. The challenge mainly examines the performance of the algorithm model, taking into account the quality of the image. Entrants need to do 2 times the image ultra-resolution processing, the algorithm complexity is controlled within 2GFLOPs, we provide a baseline model, using PSNR, SSIM and running time to synthesize the performance of the algorithm, the high score is the winner.

## Judgement Rule

For mobile platform, the image is 2 times the ultra-resolution sampling, the algorithm complexity is controlled within 2 GFLOPs, we provide a baseline model, using PSNR, SSIM and running time to synthesize the evaluation of the performance of the algorithm, the high score is the winner：

![Judgement Rule](https://github.com/AgoraIO-Community/RTC-Innovation-Challenge-2020/blob/readmeupdate/AlgorithmChanllengeProject/Judgement%20Rules.png)

Where PSNRb，SSIMb，Timeb represents the baseline model and the participant submitted model sem, SSIM, and run time, α=2，β=4，γ=0.8. The top 10 players in the final leaderboard can reach the finals, and if the overall score is below the baseline, the score is negative and they are not eligible to participate in the finals. In order to ensure fairness, participants are required to submit models to calculate PSNR, SSIM, and Time on a unified platform. 

## Resources

* Training Set (2303 pics）
* Test Set (LR，100 pics）

Participants are required to use officially provided data sets for model training, requiring participants to submit the docker environment for model training, and the PSNR of the re-realization model and the submitted entry model must not exceed ±0.5db.

*Notice:* The images provided in this contest are for training purposes only and the contestants may not be saved, disseminated or otherly non-training purposes of this contest.

We will open source scoring codes at the appropriate time, and participants can refer to the open source scoring code, submit models and self-tests.

## Schedule

July 28 - Registration, training set released.

August 12 - Test set released, participants need to submit the model before September 16.

September 16 - The front row team submits the code after the online preliminary competition, and the contest organizers conduct anti-cheating.

September 19 - Online Finals.

## Rewards

**First Prize:** 30000 RMB * One Team

**Second Prize:** 20000 RMB * One Team

**Third Prize:** 10000 RMB * One Team

**Outstanding Award：** 3000 RMB，Serval Teams

**Recruitment Green Channel** Submit your work to enter Agora recruitment green channel


## Participants

The competition is open to the whole community, domestic and foreign higher education institutions, research institutes, Internet enterprises, etc. can register to participate in the competition, the team limits for 5 people.

Persons involved in the preparation of the challenge and data are prohibited from participating. Employees of the competition organizor can participate in the ranking, but do not participate in the awards and receive the prize. 