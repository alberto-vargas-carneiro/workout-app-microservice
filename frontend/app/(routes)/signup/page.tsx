'use client'

import { useState } from "react";
import { FaArrowLeft } from "react-icons/fa";
import { IoEye, IoEyeOff } from "react-icons/io5";
import { UserDTO } from "../../models/auth";
import * as authService from "../../services/auth-service";
import style from "./page.module.css";
import Link from "next/link";

export default function SignUp() {

    const [showSuccess, setShowSuccess] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [formData, setFormData] = useState<UserDTO>({
        name: '',
        email: '',
        password: '',
        confirmPassword: ''
    })

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value })
    }


    function handleSubmit(event: React.FormEvent<EventTarget>) {
        event.preventDefault()
        authService.newUser(formData)
            .then(() => {
                setShowSuccess(true)
                setTimeout(() => {
                    window.location.href = '/'
                }, 750);
            })
            .catch(error => {
                console.log('Erro:', error.response.data)
            })
    }

    return (
        <div className={style.container}>

            <Link href={'/'}>
                <FaArrowLeft className={style.arrow} />
            </Link>

            <form className={style.formContainer} onSubmit={handleSubmit}>
                <input className={style.form}
                    name="name"
                    type="text"
                    placeholder="Name"
                    value={formData.name}
                    onChange={handleInputChange}
                />
                <input className={style.form}
                    name="email"
                    type="email"
                    placeholder="Email"
                    value={formData.email}
                    onChange={handleInputChange}
                />

                <input className={style.form}
                    name="password"
                    type={showPassword ? "text" : "password"}
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleInputChange}
                />
                <input className={style.form}
                    name="confirmPassword"
                    type={showPassword ? "text" : "password"}
                    placeholder="Confirm password"
                    value={formData.confirmPassword}
                    onChange={handleInputChange}
                />

                {showPassword ? (
                    <IoEye
                        className={style.eye}
                        onClick={() => setShowPassword(false)}
                    />
                ) : (
                    <IoEyeOff
                        className={style.eye}
                        onClick={() => setShowPassword(true)}
                    />
                )}

                {showSuccess && <div className={style.success}>Cadastro realizado com sucesso!</div>}

                <button className={style.loginButton} type="submit">Cadastrar</button>
            </form>
        </div>
    );
}