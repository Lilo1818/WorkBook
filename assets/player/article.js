/// <reference path="jquery-1.8.3.min.js" />
/// <reference path="tmpl.js" />
; (function () {
    var article = {
        isNight: 0,
        isVoted: 0,
        render: function (art, isVoted) {
            var article = art.result;//$.parseJSON(art);
            var title = '<h1>' + article.Title + '</h1>';
            var author = (!article.Author) ? '' : '<span class="author">' + article.Author + '</span>';
            var commentCount = (!article.CommentCount) ? '<span class="comment"></span>' : '<span class="comment">' + article.CommentCount + '评论</span>';
            var praiseCount = (!article.UpCount) ? '<span class="praise"></span>' : '<span class="praise">' + article.ViewCount + '阅读</span>';
            var infor = '<p class="infor">' + praiseCount + commentCount + author + '<span class="pubDate">' + article.PublishDate.toDate('yyyy-MM-dd hh:mm') + '</span></p>';
            var caption = '<div class="caption">' + title + infor + '</div>';
            var con = tmpl.render("content-tmpl", article);
            $("#content").html(caption + con);
            var dWidth = ($("#content").width() || screen.width || 800) - 28;
            $("#content").find("img").each(function (e) {
                var img = $(this);
                var origalUrl = img.attr("src");
                var imgWidth = $(this).width();
                var imgHeight = $(this).height();
                img.attr("src", "def.png").attr("orgurl", origalUrl).attr('data-index', $(this).index('img')).css({ "max-width": "100%", height: "auto", "opacity": 0 });
                img.wrap("<div class=\"img-wrap\"></div>");
					
                if (imgWidth && imgHeight) {
                    if (imgWidth > dWidth) {
                        img.css({ "width": dWidth - 10 + "px", "height": +((dWidth - 10) / imgWidth) * imgHeight + "px" });
                    } else {
                        img.css({ "width": imgWidth + "px", "height": imgHeight + "px" });
                    }
                } else {
                    var fileName = origalUrl.substr(origalUrl.lastIndexOf("/") + 1);
                    var name = fileName.substr(0, fileName.lastIndexOf("."));

                    if (name) {
                        var strs = name.split("-");
                        if (strs.length == 2) {
                            var wh = strs[strs.length - 1].split("_");
                            if (wh.length == 2) {
                                var width = parseInt(wh[0]);
                                var height = parseInt(wh[1]);
                                if (width > dWidth) {
                                    img.css({ "width": (dWidth - 10) + "px", "height": +((dWidth - 10) / width) * height + "px" });
                                } else {
                                    img.css({ "width": width + "px", "height": height + "px" });
                                }
                            }
                        }
                    }
                }

                $(this).on('click', function (e) {
                    if (!$(this).parents('a').attr('href')) {
                        window.location.href = 'mci://image?index=' + $(this).attr('data-index');
                    }
                })
            });
            
            $("iframe").each(function () {
                var iWidth = $(this).width();
                var iHeight = $(this).height();
                var rate = dWidth / iWidth;
                $(this).css({ width: dWidth + "px", height: iHeight * rate + "px" });
            });

            $('video').each(function () {
                var vWidth = $(this).width();
                var vHeight = $(this).height();
                var rate = dWidth / vWidth;
                $(this).css({ width: dWidth + "px", height: rate * vHeight + "px" });

                if (navigator.userAgent.indexOf('Android') >= 0) {
                    //$(this).removeAttr('controls');
                    $(this).on('click', function (e) {
                        e.preventDefault();
                        e.stopPropagation();
                        var videoUrl = encodeURIComponent($(this).children('source').eq(0).attr('src'));
                        if (videoUrl.toLowerCase().indexOf('.mp4') < 0) {
                            videoUrl = encodeURIComponent($(this).children('source').eq(1).attr('src'));
                        };
                        window.location.href = 'mci://video?videoUrl=' + videoUrl;
                    })
                }
            });

            //好礼
            if(article.Type === 2){
                $('#content').append('<div class="activity default"><a href="javascript:;" disabled="disabled">正在获取活动状态</a></div>');
            };

            //投票
            var vote = article.vote;
            if (vote) {
                article.isVoted = isVoted;
                if (!isVoted && vote.isExpire === 0) {
                    var voteDom = tmpl.render('vote-tmpl', vote);
                    $("#content").append(voteDom);
                    var voteOptions = [];
                    Array.prototype.indexOf = function (el) {
                        for (var i = 0, n = this.length; i < n; i++) {
                            if (this[i] === el) {
                                return i;
                            }
                        }
                        return -1;
                    }
                    $('.vote').on('touchstart', 'li', function () {
                        var id = $(this).attr('data-id');
                        var radio = $(this).find('em');
                        if (vote.VoteType == 1) {
                            if (radio.attr('class') == 'selected') {
                                radio.removeClass('selected');
                                voteOptions.splice(voteOptions.indexOf(id), 1);
                            } else {
                                radio.addClass('selected');
                                voteOptions.push($(this).attr('data-id'));
                            }
                        } else {
                            voteOptions = [];
                            $('.vote em').removeClass('selected');
                            radio.addClass('selected');
                            voteOptions.push($(this).attr('data-id'));
                        }
                        return voteOptions;
                    });
                    $('.vote').on('click', 'button', function () {
                        window.location.href = 'mci://vote?voteDetail=' + voteOptions.join(',') + '&voteId=' + vote.Id;
                        console.log(voteOptions);
                    });
                } else {
                    var votedDom = tmpl.render('voted-tmpl', vote);
                    $('#content').append(votedDom);
                }
            };

            //广告
            var ad = article.ad;
            if (ad && ad.Pic) {
                var target;
                if (ad.AdType == 2) {
                    target = ad.Url;
                } else if (ad.AdType == 4) {
                    target = ad.RefId;
                } else {
                    target = '';
                }
                var adDom = [
                    '<section class="ad">',
                    '<img data-type="' + ad.AdType + '" data-target="' + target + '" src="' + ad.Pic + '" />',
                    '</section>'
                ].join('');
                $('#content').append(adDom);
                $('.ad').on('click', 'img', function () {
                    if ($(this).attr('data-type') == '2') {
                        window.location.href = 'mci://ad?url=' + $(this).attr('data-target');
                    } else if ($(this).attr('data-type') == '4') {
                        window.location.href = 'mci://ad?articleId=' + $(this).attr('data-target');
                    }
                })
            };

            window.location.href = "mci://contentready";
        },

        //设置字体
        setFontSize: function (fontSize) {
            $("body").css({ 'font-size': fontSize + 'px' });
            article.setNightMode(article.isNight);
            return 1;
        },

        //设置行间距
        setLineHeight: function (lineHeight) {
            $('body').css({ 'line-height': lineHeight });
            article.setNightMode(article.isNight);
        },

        //图片链接
        setImgUrl: function (ourl, nurl) {
            $("#content").find("img[orgurl='" + ourl + "']").attr("src", nurl).animate({ "opacity": 1 }).parent().addClass("hide");
        },
        
        //夜间模式
        setNightMode: function (isNight) {
            article.isNight = isNight;
            if (isNight) {
                $("body").addClass("night");
            } else {
                $("body").removeClass("night");
            }
            return 1;
        },

        //投票结果
        setVotedDom: function (vote) {
            var votedDom = tmpl.render('voted-tmpl', vote.result);
            $(votedDom).insertBefore($('.vote'));
            $('.vote').remove();
        },

        //投票失败
        setVoteFailed: function () {
            alert('投票失败！');
        },
   
        //刷新赞数
        setUpCount: function (num) {
            var praiseDom = $('span.praise');
            if (praiseDom.length > 0) {
                praiseDom.html(num + '赞');
            } else {
                $('p.infor').prepend('<span class="praise">' + num + '赞</span>');
            }
        },
   
        //好礼即将开始
        setActComing: function() {
            var actComing = '<div class="activity coming"><a href="javascript:;" disabled="disabled">即将开始</a></div>';
            $(actComing).insertBefore($('.activity'));
            $('.activity').last().remove();
        },
   
        //好礼立即参与
        setActJoin: function() {
            var actJoin = '<div class="activity join"><a href="javascript:;">立即参与</a></div>';
            $(actJoin).insertBefore($('.activity'));
            $('.activity').last().remove();
            $('.join').on('click', 'a', function(){
                window.location.href = "mci://giftJoin";
            })
        },
   
        //好礼已经报名
        setActJoined: function() {
            var actJoined = '<div class="activity joined"><a href="javascript:;" disabled="disabled">已经参与</a></div>';
            $(actJoined).insertBefore($('.activity'));
            $('.activity').last().remove();
        },
   
        //好礼查看结果
        setActResult: function() {
            var actResult = '<div class="activity result"><a href="javascript:;">查看结果</a></div>';
            $(actResult).insertBefore($('.activity'));
            $('.activity').last().remove();
            $('.result').on('click', 'a', function(){
                window.location.href = "mci://giftResult";
            })
        }

    };

    window.article = article;
    $(document).ready(function () {
        window.location.href = "mci://docready";
    });
})();