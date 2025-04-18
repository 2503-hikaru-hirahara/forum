package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;

    @Autowired
    CommentService commentService;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top(@RequestParam (name = "start", required = false) LocalDate start,
                            @RequestParam (name = "end", required = false) LocalDate end) {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        CommentForm commentForm = new CommentForm();
        // 投稿を日付で絞り込み取得
        List<ReportForm> contentData = reportService.findByUpdatedDateBetween(start, end);
        // コメントを全件取得
        List<CommentForm> textData = commentService.findAllComment();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        // コメントデータオブジェクトを保管
        mav.addObject("texts", textData);
        // 準備した空のFormを保管
        mav.addObject("formModel", commentForm);
        return mav;
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") @Validated ReportForm reportForm,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<String>();
            for (FieldError error : result.getFieldErrors()) {
                 errorMessages.add(error.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/new");
        }
        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        // テーブルから投稿を削除
        reportService.deleteReport(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 編集画面表示処理
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集する投稿を取得
        ReportForm report = reportService.editReport(id);
        // 編集する投稿をセット
        mav.addObject("formModel", report);
        // 画面遷移先を指定
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * 編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent(@PathVariable Integer id,
                                      @ModelAttribute("formModel") @Validated ReportForm report,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {
        // UrlParameterのidを更新するentityにセット
        report.setId(id);
        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<String>();
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/edit/" + id);
        }
        // 編集した投稿を更新
        reportService.saveReport(report);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 新規コメント処理
     */
    @PostMapping("/comment/add")
    public ModelAndView addText(@ModelAttribute("formModel") @Validated CommentForm commentForm,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<String>();
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("formReportId", commentForm.getReportId());
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/");
        }
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * コメント削除処理
     */
    @DeleteMapping("/comment/delete/{id}")
    public ModelAndView deleteText(@PathVariable Integer id) {
        // テーブルから投稿を削除
        commentService.deleteComment(id);
        return new ModelAndView("redirect:/");
    }

    /*
     * 編集画面表示処理
     */
    @GetMapping("/comment/edit/{id}")
    public ModelAndView editText(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集する投稿を取得
        CommentForm comment = commentService.editComment(id);
        // 編集する投稿をセット
        mav.addObject("formModel", comment);
        // 画面遷移先を指定
        mav.setViewName("/comment-edit");
        return mav;
    }

    /*
     * 編集処理
     */
    @PutMapping("/comment/update/{id}")
    public ModelAndView updateText(@PathVariable Integer id,
                                   @ModelAttribute("formModel") @Validated CommentForm comment,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        // UrlParameterのidを更新するentityにセット
        comment.setId(id);
        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<String>();
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/comment/edit/" + id);
        }
        // 編集した投稿を更新
        commentService.saveComment(comment);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}
